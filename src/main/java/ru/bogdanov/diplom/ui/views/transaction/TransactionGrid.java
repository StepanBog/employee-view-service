package ru.bogdanov.diplom.ui.views.transaction;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import lombok.Getter;
import lombok.Setter;
import ru.bogdanov.diplom.backend.data.containers.Transaction;
import ru.bogdanov.diplom.backend.data.enums.TransactionStatus;
import ru.bogdanov.diplom.backend.service.ITransactionService;
import ru.bogdanov.diplom.ui.components.Badge;
import ru.bogdanov.diplom.ui.components.ColumnToggleContextMenu;
import ru.bogdanov.diplom.ui.components.grid.PaginatedGrid;
import ru.bogdanov.diplom.ui.util.IconSize;
import ru.bogdanov.diplom.ui.util.UIUtils;

import java.time.format.DateTimeFormatter;

public class TransactionGrid extends VerticalLayout {

    public static final String ID = "transactionGrid";
    protected final int PAGE_SIZE = 15;

    @Setter
    protected ITransactionService transactionService;

    protected PaginatedGrid<Transaction> grid;
    @Getter
    protected ConfigurableFilterDataProvider<Transaction, Void, Transaction> dataProvider;

    protected Transaction transactionFilter;


    public void init() {
        setId(ID);
        setSizeFull();
        initDataProvider();
        add(createContent());
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

        Button menuButton = new Button();
        menuButton.setIcon(VaadinIcon.ELLIPSIS_DOTS_H.create());
        menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ColumnToggleContextMenu columnToggleContextMenu = new ColumnToggleContextMenu(
                menuButton);
        columnToggleContextMenu.addColumnToggleItem("Статус", statusColumn);
        columnToggleContextMenu.addColumnToggleItem("Общая сумма", totalSumColumn);

        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.getHeaderRows().get(0);

        headerRow.getCell(actionColumn).setComponent(menuButton);

        return grid;
    }

    private Component createAmount(Transaction transaction) {
        Double amount = transaction.getTotalSum().movePointLeft(2).doubleValue();
        return UIUtils.createAmountLabel(amount);
    }

    public void withFilter(Transaction transactionFilter) {
        this.transactionFilter = transactionFilter;
        dataProvider.setFilter(transactionFilter);
        grid.getDataProvider().refreshAll();
    }

    private void toViewPage(Transaction transaction) {
//        UI.getCurrent().navigate(AccountDetails.class, bankAccount.getId());
    }
}
