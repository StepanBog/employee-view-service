package tech.inno.odp.ui.views.transaction;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import tech.inno.odp.backend.service.ITransactionService;
import tech.inno.odp.ui.MainLayout;
import tech.inno.odp.ui.components.FlexBoxLayout;
import tech.inno.odp.ui.layout.size.Horizontal;
import tech.inno.odp.ui.layout.size.Top;
import tech.inno.odp.ui.util.css.BoxSizing;
import tech.inno.odp.ui.views.ViewFrame;

@PageTitle("Платежи")
@Route(value = TransactionList.ROUTE, layout = MainLayout.class)
public class TransactionList extends ViewFrame {
    public static final String ROUTE = "transactions";

    @Autowired
    private ITransactionService transactionService;

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        setViewContent(createContent());
    }

    private Component createContent() {
        FlexBoxLayout content = new FlexBoxLayout(
                createGrid()
        );
        content.setBoxSizing(BoxSizing.BORDER_BOX);
        content.setHeightFull();
        content.setPadding(Horizontal.RESPONSIVE_X, Top.RESPONSIVE_X);
        return content;
    }

    private VerticalLayout createGrid() {
        TransactionGrid transactionGrid = new TransactionGrid();
        transactionGrid.setTransactionService(transactionService);
        transactionGrid.init();
        return transactionGrid;
    }
}
