package tech.inno.odp.ui.views.user;

import com.vaadin.flow.component.Component;
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
import tech.inno.odp.ui.components.field.CustomTextField;
import tech.inno.odp.ui.components.grid.PaginatedGrid;
import tech.inno.odp.ui.util.UIUtils;
import tech.inno.odp.ui.util.converter.LocalDateToLocalDateTimeConverter;
import tech.inno.odp.ui.util.converter.StringToStringWithNullValueConverter;
import tech.inno.odp.ui.util.css.lumo.BadgeColor;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
public class UserGrid extends VerticalLayout {

    private final int PAGE_SIZE = 20;

    private final IUserService userService;

    private PaginatedGrid<User> grid;
    @Getter
    private ConfigurableFilterDataProvider<User, Void, User> dataProvider;

    @PropertyId("id")
    private CustomTextField idField = new CustomTextField();
    @PropertyId("username")
    private CustomTextField usernameField = new CustomTextField();

    private ComboBox<UserRoleName> userRoleNameField = new ComboBox<>();

    @PropertyId("updatedAt")
    private DatePicker updatedAtField = new DatePicker();
    @PropertyId("createdAt")
    private DatePicker createdAtField = new DatePicker();

    private User userFilter;
    @Getter
    private BeanValidationBinder<User> binder;

    public void init() {
        setSizeFull();
        initFields();

        initDataProvider();

        this.binder = new BeanValidationBinder<>(User.class);
        this.binder.setBean(this.userFilter);

        LocalDateToLocalDateTimeConverter localDateTimeConverter = new LocalDateToLocalDateTimeConverter();
        this.binder.forField(updatedAtField)
                .withConverter(localDateTimeConverter)
                .bind(User::getUpdatedAt, User::setUpdatedAt);
        this.binder.forField(createdAtField)
                .withConverter(localDateTimeConverter)
                .bind(User::getCreatedAt, User::setCreatedAt);
        this.binder.bindInstanceFields(this);

        add(createContent());
    }

    private void initFields() {
        StringToStringWithNullValueConverter stringToStringWithNullValueConverter = new StringToStringWithNullValueConverter();

        idField.setConverters(stringToStringWithNullValueConverter);
        idField.setPlaceholder("ID");
        idField.setValueChangeMode(ValueChangeMode.EAGER);
        idField.setClearButtonVisible(true);
        idField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        idField.setWidthFull();
        idField.getStyle().set("max-width", "100%");
        idField.addValueChangeListener(e -> {
            userFilter.setId(StringUtils.isEmpty(e.getValue()) ? null : e.getValue());
            grid.getDataProvider().refreshAll();
            grid.refreshPaginator();
        });

        usernameField.setConverters(stringToStringWithNullValueConverter);
        usernameField.setPlaceholder("Username");
        usernameField.setValueChangeMode(ValueChangeMode.EAGER);
        usernameField.setClearButtonVisible(true);
        usernameField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        usernameField.setWidthFull();
        usernameField.getStyle().set("max-width", "100%");
        usernameField.addValueChangeListener(e -> {
            userFilter.setUsername(StringUtils.isEmpty(e.getValue()) ? null : e.getValue());
            grid.getDataProvider().refreshAll();
            grid.refreshPaginator();
        });


        userRoleNameField.setItems(UserRoleName.values());
        userRoleNameField.setItemLabelGenerator(UserRoleName::getDescription);
        userRoleNameField.setPlaceholder("Роль");
        userRoleNameField.addValueChangeListener(e -> {
            userFilter.setRoleNames(e.getValue() != null ? List.of(e.getValue()) : null);
            grid.getDataProvider().refreshAll();
            grid.refreshPaginator();
        });


        updatedAtField.setPlaceholder("Дата обновления");
        updatedAtField.setClearButtonVisible(true);
        updatedAtField.setWidthFull();
        updatedAtField.getStyle().set("max-width", "100%");
        updatedAtField.addValueChangeListener(
                e -> {
                    //TODO сделать фильтрацию
//                    transactionFilter.setUpdatedAt(e.getValue().atStartOfDay());
//                    grid.getDataProvider().refreshAll();
//                    grid.refreshPaginator();
                });

        createdAtField.setPlaceholder("Дата создания");
        createdAtField.setClearButtonVisible(true);
        createdAtField.setWidthFull();
        createdAtField.getStyle().set("max-width", "100%");
        createdAtField.addValueChangeListener(
                e -> {
                    //TODO сделать фильтрацию
//                    transactionFilter.setCreatedAt(e.getValue().atStartOfDay());
//                    grid.getDataProvider().refreshAll();
//                    grid.refreshPaginator();
                });
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
        grid.addSelectionListener(event -> event.getFirstSelectedItem().ifPresent(this::toViewPage));
        grid.setDataProvider(dataProvider);
        grid.setSizeFull();

        Grid.Column<User> idColumn = grid.addColumn(User::getId)
                .setWidth("200px")
                .setHeader("ID");

        Grid.Column<User> usernameColumn = grid.addColumn(User::getUsername)
                .setAutoWidth(true)
                .setComparator(User::getUsername)
                .setWidth("200px")
                .setHeader("Username")
                .setSortable(true);

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
        Grid.Column<User> rolesColumn = grid.addColumn(userRolesRenderer)
                .setAutoWidth(true)
                .setWidth("200px")
                .setHeader("Роли");

//        grid.addColumn(new ComponentRenderer<>(this::createEnabled))
//                .setAutoWidth(true)
//                .setFlexGrow(0)
//                .setComparator(User::isEnabled)
//                .setHeader("Пользователь активен")
//                .setTextAlign(ColumnTextAlign.END)
//                .setSortable(true);

        Grid.Column<User> updatedAtColumn = grid.addColumn(new LocalDateTimeRenderer<>(User::getUpdatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(User::getUpdatedAt)
                .setHeader("Дата обновления");

        Grid.Column<User> createdAtColumn = grid.addColumn(new LocalDateTimeRenderer<>(User::getCreatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(User::getCreatedAt)
                .setHeader("Дата создания");

        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();

        headerRow.getCell(idColumn).setComponent(idField);
        headerRow.getCell(usernameColumn).setComponent(usernameField);
        headerRow.getCell(rolesColumn).setComponent(userRoleNameField);
        headerRow.getCell(updatedAtColumn).setComponent(updatedAtField);
        headerRow.getCell(createdAtColumn).setComponent(createdAtField);

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

        binder.setBean(userFilter);
        binder.bindInstanceFields(this);

        if (!CollectionUtils.isEmpty(userFilter.getRoleNames())) {
            this.userRoleNameField.setValue(userFilter.getRoleNames().get(0));
        } else {
            this.userRoleNameField.setValue(null);
        }

        dataProvider.setFilter(userFilter);
        grid.getDataProvider().refreshAll();
    }

    private void toViewPage(User user) {
//        UI.getCurrent().navigate(AccountDetails.class, bankAccount.getId());
    }
}
