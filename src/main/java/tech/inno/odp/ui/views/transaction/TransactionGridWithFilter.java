package tech.inno.odp.ui.views.transaction;

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
import tech.inno.odp.backend.data.containers.Transaction;
import tech.inno.odp.backend.data.enums.TransactionStatus;
import tech.inno.odp.ui.components.Badge;
import tech.inno.odp.ui.components.ColumnToggleContextMenu;
import tech.inno.odp.ui.components.grid.PaginatedGrid;
import tech.inno.odp.ui.util.IconSize;
import tech.inno.odp.ui.util.UIUtils;
import tech.inno.odp.ui.util.converter.LocalDateToLocalDateTimeConverter;

import java.time.format.DateTimeFormatter;

public class TransactionGridWithFilter extends TransactionGrid{
    public static final String ID = "transactionGridWithFilter";
    @PropertyId("id")
    protected TextField idField = new TextField();
    @PropertyId("status")
    protected ComboBox<TransactionStatus> statusField = new ComboBox();
    @PropertyId("totalSum")
    protected BigDecimalField totalSumField = new BigDecimalField();

    @PropertyId("updatedAt")
    protected DatePicker updatedAtField = new DatePicker();
    @PropertyId("createdAt")
    protected DatePicker createdAtField = new DatePicker();
    @Getter
    private BeanValidationBinder<Transaction> binder;


    public void init() {
        setId(ID);
        setSizeFull();
        initFields();

        initDataProvider();

        this.binder = new BeanValidationBinder<>(Transaction.class);
        this.binder.setBean(this.transactionFilter);

        LocalDateToLocalDateTimeConverter localDateTimeConverter = new LocalDateToLocalDateTimeConverter();
        this.binder.forField(updatedAtField)
                .withConverter(localDateTimeConverter)
                .bind(Transaction::getUpdatedAt, Transaction::setUpdatedAt);
        this.binder.forField(createdAtField)
                .withConverter(localDateTimeConverter)
                .bind(Transaction::getCreatedAt, Transaction::setCreatedAt);
        this.binder.bindInstanceFields(this);

        add(createContent());
    }

    private void initFields() {
        idField.setPlaceholder("ID");
        idField.setValueChangeMode(ValueChangeMode.EAGER);
        idField.setClearButtonVisible(true);
        idField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        idField.setWidthFull();
        idField.getStyle().set("max-width", "100%");
        idField.addValueChangeListener(e -> {
            transactionFilter.setId(StringUtils.isEmpty(e.getValue()) ? null : e.getValue());
            grid.getDataProvider().refreshAll();
            grid.refreshPaginator();
        });

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

        updatedAtField.setPlaceholder("Дата обновления");
        updatedAtField.setClearButtonVisible(true);
        updatedAtField.setWidthFull();
        updatedAtField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        updatedAtField.getStyle().set("max-width", "100%");
        updatedAtField.addValueChangeListener(
                e -> {
                    transactionFilter.setUpdatedAt(e.getValue().atStartOfDay());
                    grid.getDataProvider().refreshAll();
                    grid.refreshPaginator();
                });

        createdAtField.setPlaceholder("Дата создания");
        createdAtField.setClearButtonVisible(true);
        createdAtField.setWidthFull();
        createdAtField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        createdAtField.getStyle().set("max-width", "100%");
        createdAtField.addValueChangeListener(
                e -> {
                    transactionFilter.setCreatedAt(e.getValue().atStartOfDay());
                    grid.getDataProvider().refreshAll();
                    grid.refreshPaginator();
                });
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
        ComponentRenderer<Button, Transaction> actionRenderer = new ComponentRenderer<>(
                transaction -> {
                    Button editButton = UIUtils.createButton(VaadinIcon.EDIT,
                            ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_SMALL);
                    editButton.addClassName(IconSize.XS.getClassName());
                    editButton.addClickListener(event -> toViewPage(transaction));
                    return editButton;
                }
        );

        Grid.Column<Transaction> actionColumn = grid.addColumn(actionRenderer)
                .setFrozen(true)
                .setFlexGrow(0)
                .setWidth("100px")
                .setHeader("Действие")
                .setResizable(true);

        Grid.Column<Transaction> idColumn = grid.addColumn(Transaction::getId)
                .setAutoWidth(true)
                .setWidth("100px")
                .setHeader("ID")
                .setSortable(true)
                .setComparator(Transaction::getId)
                .setResizable(true);
        idColumn.setVisible(false);

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
        columnToggleContextMenu.addColumnToggleItem("id", idColumn);
        columnToggleContextMenu.addColumnToggleItem("Статус", statusColumn);
        columnToggleContextMenu.addColumnToggleItem("Общая сумма", totalSumColumn);
        columnToggleContextMenu.addColumnToggleItem("Дата обновления", updatedAtColumn);
        columnToggleContextMenu.addColumnToggleItem("Дата создания", createdAtColumn);

        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();

        headerRow.getCell(actionColumn).setComponent(menuButton);
        headerRow.getCell(idColumn).setComponent(idField);
        headerRow.getCell(statusColumn).setComponent(statusField);
        headerRow.getCell(totalSumColumn).setComponent(totalSumField);
        headerRow.getCell(updatedAtColumn).setComponent(updatedAtField);
        headerRow.getCell(createdAtColumn).setComponent(createdAtField);

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

