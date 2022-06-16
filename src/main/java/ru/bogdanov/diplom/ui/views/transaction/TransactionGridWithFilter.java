package ru.bogdanov.diplom.ui.views.transaction;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import ru.bogdanov.diplom.backend.data.containers.Requisites;
import ru.bogdanov.diplom.backend.data.containers.Transaction;
import ru.bogdanov.diplom.backend.data.enums.TransactionStatus;
import ru.bogdanov.diplom.ui.components.Badge;
import ru.bogdanov.diplom.ui.components.ColumnToggleContextMenu;
import ru.bogdanov.diplom.ui.components.grid.PaginatedGrid;
import ru.bogdanov.diplom.ui.util.IconSize;
import ru.bogdanov.diplom.ui.util.UIUtils;
import ru.bogdanov.diplom.ui.util.converter.LocalDateToLocalDateTimeConverter;
import ru.bogdanov.diplom.ui.views.transaction.form.DeclineTransactionDialog;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class TransactionGridWithFilter extends TransactionGrid{
    public static final String ID = "transactionGridWithFilter";
    @PropertyId("status")
    protected ComboBox<TransactionStatus> statusField = new ComboBox();
    @PropertyId("totalSum")
    protected BigDecimalField totalSumField = new BigDecimalField();
    @Getter
    private BeanValidationBinder<Transaction> binder;

    private boolean dialogIsOpen = false;


    public void init() {
        setId(ID);
        setSizeFull();
        initFields();

        initDataProvider();

        this.binder = new BeanValidationBinder<>(Transaction.class);
        this.binder.setBean(this.transactionFilter);

        this.binder.bindInstanceFields(this);

        add(createContent());
    }

    private void initFields() {

        statusField.setPlaceholder("Статус");
        statusField.setItems(TransactionStatus.values());
        statusField.setItemLabelGenerator(TransactionStatus::getDescription);
        statusField.setClearButtonVisible(true);
        statusField.setWidthFull();
        statusField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        statusField.getStyle().set("max-width", "100%");
        statusField.addValueChangeListener(
                s -> {
                    transactionFilter.setStatus(s.getValue());
                    grid.getDataProvider().refreshAll();
                    grid.refreshPaginator();
                }
        );

        totalSumField.setPlaceholder("Общая сумма");
        totalSumField.setClearButtonVisible(true);
        totalSumField.setWidthFull();
        totalSumField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        totalSumField.getStyle().set("max-width", "100%");
        totalSumField.addValueChangeListener(
                s -> {
                    transactionFilter.setTotalSum(s.getValue());
                    grid.getDataProvider().refreshAll();
                    grid.refreshPaginator();
                }
        );
    }

    private Component createContent() {

        VerticalLayout content = new VerticalLayout(
                createGrid()
        );

        content.setBoxSizing(BoxSizing.BORDER_BOX);
        content.setHeightFull();
        content.setPadding(false);
        content.setMargin(false);
        content.setSpacing(false);
        return content;
    }

    private void initDataProvider() {
        this.dataProvider = new CallbackDataProvider<Transaction, Transaction>(
                query -> transactionService.find(query, PAGE_SIZE).stream(),
                query -> transactionService.getTotalCount(query))
                .withConfigurableFilter();

        this.transactionFilter = Transaction.builder()
                .status(null)
                .build();
        this.dataProvider.setFilter(this.transactionFilter);
    }


    private Grid<Transaction> createGrid() {
        grid = new PaginatedGrid<>();
        grid.addSelectionListener(action -> {
            if (!dialogIsOpen) {
                dialogIsOpen = true;
                DeclineTransactionDialog dialog = new DeclineTransactionDialog();
                Transaction transaction = transactionService.findById(UUID.fromString(action.getFirstSelectedItem().get().getId()));
                dialog.init();
                dialog.withBean(transaction);
                dialog.setTransactionService(transactionService);
                dialog.setDeclineAction(decline -> {
                    dataProvider.refreshAll();
                    dialogIsOpen = false;
                });
                dialog.setCancelAction(decline -> {
                    dataProvider.refreshAll();
                    dialogIsOpen = false;
                });
                dialog.open();
            }
        });
        grid.setPageSize(PAGE_SIZE);
        grid.setDataProvider(dataProvider);
        grid.setSizeFull();

        ComponentRenderer<Badge, Transaction> badgeRenderer = new ComponentRenderer<>(
                transaction -> {
                    TransactionStatus status = transaction.getStatus();
                    Badge badge = new Badge(status.getDescription(), transaction.getStatusTheme());
                    return badge;
                }
        );

        Grid.Column<Transaction> statusColumn = grid.addColumn(badgeRenderer)
                .setAutoWidth(true)
                .setComparator(Transaction::getStatus)
                .setWidth("200px")
                .setHeader("Статус")
                .setSortable(true)
                .setResizable(true);

        Grid.Column<Transaction> totalSumColumn = grid.addColumn(new ComponentRenderer<>(this::createAmount))
                .setWidth("200px")
                .setComparator(Transaction::getTotalSum)
                .setHeader("Общая сумма")
                .setSortable(true)
                .setResizable(true);
        Grid.Column<Transaction> updatedAtColumn = grid.addColumn(new LocalDateTimeRenderer<>(Transaction::getUpdatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Transaction::getUpdatedAt)
                .setHeader("Дата обновления")
                .setResizable(true);

        Grid.Column<Transaction> createdAtColumn = grid.addColumn(new LocalDateTimeRenderer<>(Transaction::getCreatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Transaction::getCreatedAt)
                .setHeader("Дата создания")
                .setResizable(true);

        Button menuButton = new Button();
        menuButton.setIcon(VaadinIcon.ELLIPSIS_DOTS_H.create());
        menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ColumnToggleContextMenu columnToggleContextMenu = new ColumnToggleContextMenu(
                menuButton);
        columnToggleContextMenu.addColumnToggleItem("Статус", statusColumn);
        columnToggleContextMenu.addColumnToggleItem("Общая сумма", totalSumColumn);
        columnToggleContextMenu.addColumnToggleItem("Дата обновления", updatedAtColumn);
        columnToggleContextMenu.addColumnToggleItem("Дата создания", createdAtColumn);

        return grid;
    }

    private Component createAmount(Transaction transaction) {
        Double amount = transaction.getTotalSum().movePointLeft(2).doubleValue();
        return UIUtils.createAmountLabel(amount);
    }

    public void withFilter(Transaction transactionFilter) {
        this.transactionFilter = transactionFilter;
        dataProvider.setFilter(transactionFilter);
        binder.setBean(transactionFilter);
        binder.bindInstanceFields(this);


        grid.getDataProvider().refreshAll();
    }

    private void toViewPage(Transaction transaction) {
//        UI.getCurrent().navigate(AccountDetails.class, bankAccount.getId());
    }
}

