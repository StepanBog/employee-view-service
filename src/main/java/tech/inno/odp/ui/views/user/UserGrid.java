package tech.inno.odp.ui.views.user;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import tech.inno.odp.backend.data.containers.User;
import tech.inno.odp.backend.data.enums.UserRoleName;
import tech.inno.odp.backend.service.IUserService;
import tech.inno.odp.ui.components.Badge;
import tech.inno.odp.ui.components.ColumnToggleContextMenu;
import tech.inno.odp.ui.components.field.CustomTextField;
import tech.inno.odp.ui.components.grid.PaginatedGrid;
import tech.inno.odp.ui.util.IconSize;
import tech.inno.odp.ui.util.UIUtils;
import tech.inno.odp.ui.util.css.lumo.BadgeColor;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
public class UserGrid extends VerticalLayout {
    protected final int PAGE_SIZE = 15;

    protected final IUserService userService;

    protected PaginatedGrid<User> grid;

    @Getter
    protected ConfigurableFilterDataProvider<User, Void, User> dataProvider;

    protected User userFilter;

    public void init() {
        setSizeFull();
        initDataProvider();
        add(createContent());
    }

    private Component createContent() {

        VerticalLayout content = new VerticalLayout(
                createGrid()
        );

        content.setBoxSizing(BoxSizing.BORDER_BOX);
        content.setHeightFull();
        content.setPadding(false);
        content.setMargin(false);
        content.setSpacing(false);
        return content;
    }

    private void initDataProvider() {
        this.dataProvider = new CallbackDataProvider<User, User>(
                query -> userService.find(query, PAGE_SIZE).stream(),
                query -> userService.getTotalCount(query))
                .withConfigurableFilter();

        this.userFilter = User.builder()
                .build();
        this.dataProvider.setFilter(this.userFilter);
    }


    private Grid<User> createGrid() {
        grid = new PaginatedGrid<>();
        grid.setPageSize(PAGE_SIZE);
        grid.setDataProvider(dataProvider);
        grid.setSizeFull();

        ComponentRenderer<Button, User> actionRenderer = new ComponentRenderer<>(
                user -> {
                    Button editButton = UIUtils.createButton(VaadinIcon.EDIT,
                            ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_SMALL);
                    editButton.addClassName(IconSize.XS.getClassName());
                    editButton.addClickListener(event -> toViewPage(user));
                    return editButton;
                }
        );

        ComponentRenderer<VerticalLayout, User> userRolesRenderer = new ComponentRenderer<>(
                users -> {
                    VerticalLayout layout = new VerticalLayout();
                    if (!CollectionUtils.isEmpty(users.getRoleNames())) {
                        users.getRoleNames()
                                .forEach(userRoleName -> {
                                    Badge badge = new Badge(userRoleName.name(), BadgeColor.NORMAL);
                                    layout.add(badge);
                                });
                    }
                    layout.setSpacing(false);
                    layout.setPadding(false);
                    layout.setMargin(false);
                    return layout;
                }
        );

        Grid.Column<User> actionColumn = grid.addColumn(actionRenderer)
                .setFrozen(true)
                .setFlexGrow(0)
                .setWidth("100px")
                .setHeader("Действие");

        Grid.Column<User> idColumn = grid.addColumn(User::getId)
                .setAutoWidth(true)
                .setWidth("100px")
                .setHeader("ID")
                .setSortable(true)
                .setResizable(true)
                .setComparator(User::getId);
        idColumn.setVisible(false);

        Grid.Column<User> usernameColumn = grid.addColumn(User::getUsername)
                .setAutoWidth(true)
                .setComparator(User::getUsername)
                .setWidth("200px")
                .setHeader("Username")
                .setSortable(true)
                .setResizable(true);
        Grid.Column<User> rolesColumn = grid.addColumn(userRolesRenderer)
                .setAutoWidth(true)
                .setWidth("200px")
                .setHeader("Роли")
                .setResizable(true);

        Grid.Column<User> updatedAtColumn = grid.addColumn(new LocalDateTimeRenderer<>(User::getUpdatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(User::getUpdatedAt)
                .setResizable(true)
                .setHeader("Дата обновления");

        Grid.Column<User> createdAtColumn = grid.addColumn(new LocalDateTimeRenderer<>(User::getCreatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(User::getCreatedAt)
                .setResizable(true)
                .setHeader("Дата создания");

        Button menuButton = new Button();
        menuButton.setIcon(VaadinIcon.ELLIPSIS_DOTS_H.create());
        menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ColumnToggleContextMenu columnToggleContextMenu = new ColumnToggleContextMenu(
                menuButton);
        columnToggleContextMenu.addColumnToggleItem("id", idColumn);
        columnToggleContextMenu.addColumnToggleItem("Username", usernameColumn);
        columnToggleContextMenu.addColumnToggleItem("Роли", rolesColumn);
        columnToggleContextMenu.addColumnToggleItem("Дата обновления", updatedAtColumn);
        columnToggleContextMenu.addColumnToggleItem("Дата создания", createdAtColumn);


        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.getHeaderRows().get(0);

        headerRow.getCell(actionColumn).setComponent(menuButton);

        return grid;
    }

    private Component createEnabled(User user) {
        Icon icon;
        if (user.isEnabled()) {
            icon = UIUtils.createPrimaryIcon(VaadinIcon.CHECK);
        } else {
            icon = UIUtils.createDisabledIcon(VaadinIcon.CLOSE);
        }
        return icon;
    }

    public void withFilter(User userFilter) {
        this.userFilter = userFilter;
        dataProvider.setFilter(userFilter);
        grid.getDataProvider().refreshAll();
    }

    private void toViewPage(User user) {
        UI.getCurrent().navigate(UserView.class, user.getId());
    }
}
