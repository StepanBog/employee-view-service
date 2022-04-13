package tech.inno.odp.ui.views.user;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import tech.inno.odp.backend.service.IEmployerService;
import tech.inno.odp.backend.service.IUserService;
import tech.inno.odp.ui.MainLayout;
import tech.inno.odp.ui.components.FlexBoxLayout;
import tech.inno.odp.ui.layout.SplitLayoutToggle;
import tech.inno.odp.ui.util.css.BoxSizing;
import tech.inno.odp.ui.views.ViewFrame;
import tech.inno.odp.ui.views.user.form.UserSearchInGridForm;

import javax.annotation.PostConstruct;

@PageTitle("Пользователи")
@Route(value = UserList.ROUTE, layout = MainLayout.class)
@UIScope
@RequiredArgsConstructor
public class UserList extends ViewFrame {
    public static final String ROUTE = "users";

    private final IUserService userService;
    private final IEmployerService employerService;

    @PostConstruct
    public void init() {
        setViewContent(createContent());
    }

    private Component createContent() {
        UserGrid grid = createGrid();
        UserSearchInGridForm searchForm = new UserSearchInGridForm(employerService, grid);
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

    private UserGrid createGrid() {
        UserGrid userGrid= new UserGrid(userService);
        userGrid.init();
        return userGrid;
    }
}
