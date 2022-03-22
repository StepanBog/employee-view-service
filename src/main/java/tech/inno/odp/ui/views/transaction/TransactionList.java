package tech.inno.odp.ui.views.transaction;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.data.containers.Transaction;
import tech.inno.odp.backend.data.enums.TransactionStatus;
import tech.inno.odp.ui.MainLayout;
import tech.inno.odp.ui.components.Badge;
import tech.inno.odp.ui.components.FlexBoxLayout;
import tech.inno.odp.ui.layout.size.Horizontal;
import tech.inno.odp.ui.layout.size.Top;
import tech.inno.odp.ui.provider.TransactionDataProvider;
import tech.inno.odp.ui.util.UIUtils;
import tech.inno.odp.ui.util.css.BoxSizing;
import tech.inno.odp.ui.views.ViewFrame;

import java.time.format.DateTimeFormatter;

@PageTitle("Платежи")
@Route(value = "transactions", layout = MainLayout.class)
public class TransactionList extends ViewFrame {

    private Grid<Transaction> grid;

    @Autowired
    private TransactionDataProvider transactionDataProvider;

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        setViewContent(createContent());
    }

    private Component createContent() {
        FlexBoxLayout content = new FlexBoxLayout(createGrid());
        content.setBoxSizing(BoxSizing.BORDER_BOX);
        content.setHeightFull();
        content.setPadding(Horizontal.RESPONSIVE_X, Top.RESPONSIVE_X);
        return content;
    }

    private Grid<Transaction> createGrid() {
        grid = new Grid<>();
        grid.setPageSize(50);
        grid.addSelectionListener(event -> event.getFirstSelectedItem().ifPresent(this::toViewPage));
        grid.setDataProvider(transactionDataProvider);
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
