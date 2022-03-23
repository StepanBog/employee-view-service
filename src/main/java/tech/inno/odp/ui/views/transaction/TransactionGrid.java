package tech.inno.odp.ui.views.transaction;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import lombok.Setter;
import tech.inno.odp.backend.data.containers.Transaction;
import tech.inno.odp.backend.data.enums.TransactionStatus;
import tech.inno.odp.backend.service.ITransactionService;
import tech.inno.odp.ui.components.Badge;
import tech.inno.odp.ui.components.grid.PaginatedGrid;
import tech.inno.odp.ui.util.UIUtils;

import java.time.format.DateTimeFormatter;

public class TransactionGrid extends VerticalLayout {

    private final int PAGE_SIZE = 20;

    @Setter
    private ITransactionService transactionService;

    private PaginatedGrid<Transaction> grid;
    private ConfigurableFilterDataProvider<Transaction, Void, Transaction> dataProvider;
    private Transaction transactionFilter;

    public void init() {
        setSizeFull();
        add(createContent());
    }

    private Component createContent() {
        initDataProvider();

        VerticalLayout content = new VerticalLayout(
                createGrid()
        );

        content.setBoxSizing(BoxSizing.BORDER_BOX);
        content.setHeightFull();
        content.setPadding(true);
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
        grid.setPageSize(50);
        grid.addSelectionListener(event -> event.getFirstSelectedItem().ifPresent(this::toViewPage));
        grid.setDataProvider(dataProvider);
        grid.setSizeFull();

        ComponentRenderer<Badge, Transaction> badgeRenderer = new ComponentRenderer<>(
                transaction -> {
                    TransactionStatus status = transaction.getStatus();
                    Badge badge = new Badge(status.name(), transaction.getStatusTheme());
//                    UIUtils.setTooltip(status.getDesc(), badge);
                    return badge;
                }
        );
        Grid.Column<Transaction> idColumn = grid.addColumn(Transaction::getId)
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setFrozen(true)
                .setHeader("ID");

        grid.addColumn(badgeRenderer)
                .setFrozen(true)
                .setWidth("200px")
                .setHeader("Статус");
        grid.addColumn(new ComponentRenderer<>(this::createAmount))
                .setWidth("200px")
                .setHeader("Общая сумма");
        grid.addColumn(new LocalDateTimeRenderer<>(Transaction::getUpdatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Transaction::getUpdatedAt)
                .setHeader("Дата обновления");
        grid.addColumn(new LocalDateTimeRenderer<>(Transaction::getCreatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Transaction::getCreatedAt)
                .setHeader("Дата создания");

        return grid;
    }

    private Component createAmount(Transaction transaction) {
        Double amount = transaction.getTotalSum().movePointLeft(2).doubleValue();
        return UIUtils.createAmountLabel(amount);
    }

    private void toViewPage(Transaction transaction) {
//        UI.getCurrent().navigate(AccountDetails.class, bankAccount.getId());
    }
}
