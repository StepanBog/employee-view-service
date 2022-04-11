package tech.inno.odp.ui.views.employer;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import tech.inno.odp.backend.service.IEmployerService;
import tech.inno.odp.ui.MainLayout;
import tech.inno.odp.ui.components.FlexBoxLayout;
import tech.inno.odp.ui.layout.SplitLayoutToggle;
import tech.inno.odp.ui.util.css.BoxSizing;
import tech.inno.odp.ui.views.ViewFrame;
import tech.inno.odp.ui.views.employer.form.EmployerSearchInGridForm;

@PageTitle("Работодатель")
@Route(value = EmployerList.ROUTE, layout = MainLayout.class)
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
