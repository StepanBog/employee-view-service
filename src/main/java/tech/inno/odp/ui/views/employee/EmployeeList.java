package tech.inno.odp.ui.views.employee;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import tech.inno.odp.backend.service.IEmployeeService;
import tech.inno.odp.backend.service.IEmployerService;
import tech.inno.odp.ui.MainLayout;
import tech.inno.odp.ui.components.FlexBoxLayout;
import tech.inno.odp.ui.layout.size.Horizontal;
import tech.inno.odp.ui.layout.size.Top;
import tech.inno.odp.ui.util.css.BoxSizing;
import tech.inno.odp.ui.views.ViewFrame;

@PageTitle("Работники")
@Route(value = EmployeeList.ROUTE, layout = MainLayout.class)
@RequiredArgsConstructor
public class EmployeeList extends ViewFrame {

    public static final String ROUTE = "employees";

    private final IEmployeeService employeeService;
    private final IEmployerService employerService;

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
        EmployeeGrid employeeGrid = new EmployeeGrid();
        employeeGrid.setEmployeeService(employeeService);
        employeeGrid.setEmployerService(employerService);
        employeeGrid.init();

        return employeeGrid;
    }
}
