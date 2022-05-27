package ru.bogdanov.diplom.ui.views.employer;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import ru.bogdanov.diplom.backend.service.IEmployerService;
import ru.bogdanov.diplom.ui.EmployeeMainLayout;
import ru.bogdanov.diplom.ui.components.FlexBoxLayout;
import ru.bogdanov.diplom.ui.layout.SplitLayoutToggle;
import ru.bogdanov.diplom.ui.util.css.BoxSizing;
import ru.bogdanov.diplom.ui.views.ViewFrame;
import ru.bogdanov.diplom.ui.views.employer.form.EmployerSearchInGridForm;

@PageTitle("Работодатель")
@Route(value = EmployerList.ROUTE, layout = EmployeeMainLayout.class)
@RequiredArgsConstructor
public class EmployerList extends ViewFrame {

    public static final String ROUTE = "employers";

    private final IEmployerService employerService;

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        setViewContent(createContent());
    }

    private Component createContent() {
        EmployerGrid grid = createGrid();
        EmployerSearchInGridForm searchForm = new EmployerSearchInGridForm(grid);
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

    private EmployerGrid createGrid() {
        EmployerGrid employerGrid = new EmployerGrid();
        employerGrid.setEmployerService(employerService);
        employerGrid.init();
        return employerGrid;
    }
}
