package tech.inno.odp.ui.views.transaction;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import tech.inno.odp.backend.service.IEmployerService;
import tech.inno.odp.backend.service.ITransactionService;
import tech.inno.odp.ui.MainLayout;
import tech.inno.odp.ui.components.FlexBoxLayout;
import tech.inno.odp.ui.layout.SplitLayoutToggle;
import tech.inno.odp.ui.util.css.BoxSizing;
import tech.inno.odp.ui.views.ViewFrame;
import tech.inno.odp.ui.views.transaction.form.TransactionSearchInGridForm;

import javax.annotation.PostConstruct;

@PageTitle("Платежи")
@Route(value = TransactionList.ROUTE, layout = MainLayout.class)
@UIScope
@RequiredArgsConstructor
public class TransactionList extends ViewFrame {
    public static final String ROUTE = "transactions";

    private final ITransactionService transactionService;
    private final IEmployerService employerService;

    @PostConstruct
    public void init() {
        setViewContent(createContent());
    }

    private Component createContent() {
        TransactionGrid grid = createGrid();
        TransactionSearchInGridForm searchForm = new TransactionSearchInGridForm(employerService, grid);
        searchForm.init();

        SplitLayoutToggle splitLayoutToggle = new SplitLayoutToggle(
                searchForm,
                grid
        );

        FlexBoxLayout content = new FlexBoxLayout(
                splitLayoutToggle
        );
        content.setBoxSizing(BoxSizing.BORDER_BOX);
        content.setSizeFull();
        return content;
    }

    private TransactionGrid createGrid() {
        TransactionGrid transactionGrid = new TransactionGrid();
        transactionGrid.setTransactionService(transactionService);
        transactionGrid.init();
        return transactionGrid;
    }
}
