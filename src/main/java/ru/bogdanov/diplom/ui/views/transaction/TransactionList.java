package ru.bogdanov.diplom.ui.views.transaction;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import ru.bogdanov.diplom.backend.service.IEmployerService;
import ru.bogdanov.diplom.backend.service.ITransactionService;
import ru.bogdanov.diplom.ui.EmployeeMainLayout;
import ru.bogdanov.diplom.ui.components.FlexBoxLayout;
import ru.bogdanov.diplom.ui.layout.SplitLayoutToggle;
import ru.bogdanov.diplom.ui.util.css.BoxSizing;
import ru.bogdanov.diplom.ui.views.ViewFrame;
import ru.bogdanov.diplom.ui.views.transaction.form.TransactionSearchInGridForm;

import javax.annotation.PostConstruct;

@PageTitle("Платежи")
@Route(value = TransactionList.ROUTE, layout = EmployeeMainLayout.class)
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
