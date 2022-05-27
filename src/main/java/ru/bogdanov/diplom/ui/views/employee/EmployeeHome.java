package ru.bogdanov.diplom.ui.views.employee;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.bogdanov.diplom.ui.EmployeeMainLayout;
import ru.bogdanov.diplom.ui.components.FlexBoxLayout;
import ru.bogdanov.diplom.ui.layout.size.Horizontal;
import ru.bogdanov.diplom.ui.layout.size.Uniform;
import ru.bogdanov.diplom.ui.views.ViewFrame;

@PageTitle("Welcome")
@Route(value = EmployeeHome.ROUTE, layout = EmployeeMainLayout.class)
public class EmployeeHome extends ViewFrame {

    public static final String ROUTE = "employee";

    public EmployeeHome() {
        setId("employeeHome");
        setViewContent(createContent());
    }

    private Component createContent() {
        Html intro = new Html("<p>Стартовая страница</p>");

        FlexBoxLayout content = new FlexBoxLayout(intro);
        content.setFlexDirection(FlexDirection.COLUMN);
        content.setMargin(Horizontal.AUTO);
        content.setMaxWidth("840px");
        content.setPadding(Uniform.RESPONSIVE_L);
        return content;
    }

}
