package tech.inno.odp.ui.views.employee;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import tech.inno.odp.backend.service.IEmployeeService;
import tech.inno.odp.backend.service.IEmployerService;
import tech.inno.odp.ui.MainLayout;
import tech.inno.odp.ui.components.FlexBoxLayout;
import tech.inno.odp.ui.layout.SplitLayoutToggle;
import tech.inno.odp.ui.util.css.BoxSizing;
import tech.inno.odp.ui.views.ViewFrame;
import tech.inno.odp.ui.views.employee.form.EmployeeSearchInGridForm;

import javax.annotation.PostConstruct;

@PageTitle("Работники")
@Route(value = EmployeeList.ROUTE, layout = MainLayout.class)
@RequiredArgsConstructor
public class EmployeeList extends ViewFrame {

    public static final String ROUTE = "employees";

    private final IEmployeeService employeeService;
    private final IEmployerService employerService;

    @PostConstruct
    public void init() {
        setViewContent(createContent());
    }

    private Component createContent() {
        EmployeeGrid grid = createGrid();
        EmployeeSearchInGridForm searchForm = new EmployeeSearchInGridForm(employerService, grid);
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

    private EmployeeGrid createGrid() {
        EmployeeGrid employeeGrid = new EmployeeGrid();
        employeeGrid.setEmployeeService(employeeService);
        employeeGrid.setEmployerService(employerService);
        employeeGrid.init();
        return employeeGrid;
    }
}
