package tech.inno.odp.ui.views.employee;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.QueryParameters;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import tech.inno.odp.backend.data.containers.Employee;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.data.enums.EmployeeStatus;
import tech.inno.odp.backend.service.IEmployeeService;
import tech.inno.odp.backend.service.IEmployerService;
import tech.inno.odp.grpc.generated.service.employer.SearchEmployerRequest;
import tech.inno.odp.ui.components.Badge;
import tech.inno.odp.ui.components.ColumnToggleContextMenu;
import tech.inno.odp.ui.components.field.CustomTextField;
import tech.inno.odp.ui.components.grid.PaginatedGrid;
import tech.inno.odp.ui.util.IconSize;
import tech.inno.odp.ui.util.UIUtils;
import tech.inno.odp.ui.util.converter.LocalDateToLocalDateTimeConverter;
import tech.inno.odp.ui.util.converter.StringToStringWithNullValueConverter;

import java.time.format.DateTimeFormatter;
import java.util.Map;

public class EmployeeGrid extends VerticalLayout {

    public static final String ID = "employeeGrid";
    private final int PAGE_SIZE = 15;

    @Setter
    private IEmployeeService employeeService;
    @Setter
    private IEmployerService employerService;
    @Setter
    private boolean fromEmployer = false;

    @PropertyId("id")
    private CustomTextField idField = new CustomTextField();
    @PropertyId("status")
    private ComboBox<EmployeeStatus> statusField = new ComboBox();
    @PropertyId("lastName")
    private CustomTextField lastNameNameField = new CustomTextField();
    @PropertyId("firstName")
    private CustomTextField firstNameField = new CustomTextField();
    @PropertyId("patronymicName")
    private CustomTextField patronymicNameField = new CustomTextField();

    private ComboBox<Employer> employerField = new ComboBox<>();

    @PropertyId("updatedAt")
    private DatePicker updatedAtField = new DatePicker();
    @PropertyId("createdAt")
    private DatePicker createdAtField = new DatePicker();

    @Getter
    private BeanValidationBinder<Employee> binder;

    @Getter
    private Employee employeeFilter;

    private PaginatedGrid<Employee> grid;
    private ConfigurableFilterDataProvider<Employee, Void, Employee> dataProvider;

    public void init() {
        setId(ID);
        setSizeFull();
        initFields();

        initDataProvider();

        this.binder = new BeanValidationBinder<>(Employee.class);
        this.binder.setBean(this.employeeFilter);

        LocalDateToLocalDateTimeConverter localDateTimeConverter = new LocalDateToLocalDateTimeConverter();
        this.binder.forField(updatedAtField)
                .withConverter(localDateTimeConverter)
                .bind(Employee::getUpdatedAt, Employee::setUpdatedAt);
        this.binder.forField(createdAtField)
                .withConverter(localDateTimeConverter)
                .bind(Employee::getCreatedAt, Employee::setCreatedAt);
        this.binder.bindInstanceFields(this);

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

    private void initFields() {

        StringToStringWithNullValueConverter stringToStringWithNullValueConverter = new StringToStringWithNullValueConverter();

        idField.setConverters(stringToStringWithNullValueConverter);
        idField.setPlaceholder("ID");
        idField.setValueChangeMode(ValueChangeMode.EAGER);
        idField.setClearButtonVisible(true);
        idField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        idField.setWidthFull();
        idField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        idField.getStyle().set("max-width", "100%");
        idField.addValueChangeListener(e -> {
            employeeFilter.setId(StringUtils.isEmpty(e.getValue()) ? null : e.getValue());
            grid.getDataProvider().refreshAll();
            grid.refreshPaginator();
        });

        statusField.setPlaceholder("Статус");
        statusField.setItems(EmployeeStatus.values());
        statusField.setItemLabelGenerator(EmployeeStatus::getDescription);
        statusField.setClearButtonVisible(true);
        statusField.setWidthFull();
        statusField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        statusField.getStyle().set("max-width", "100%");
        statusField.addValueChangeListener(
                s -> {
                    employeeFilter.setStatus(s.getValue());
                    grid.getDataProvider().refreshAll();
                    grid.refreshPaginator();
                }
        );

        lastNameNameField.setConverters(stringToStringWithNullValueConverter);
        lastNameNameField.setPlaceholder("Фамилия");
        lastNameNameField.setClearButtonVisible(true);
        lastNameNameField.setWidthFull();
        lastNameNameField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        lastNameNameField.getStyle().set("max-width", "100%");
        lastNameNameField.addValueChangeListener(
                s -> {
                    employeeFilter.setLastName(s.getValue());
                    grid.getDataProvider().refreshAll();
                    grid.refreshPaginator();
                }
        );

        firstNameField.setConverters(stringToStringWithNullValueConverter);
        firstNameField.setPlaceholder("Имя");
        firstNameField.setClearButtonVisible(true);
        firstNameField.setWidthFull();
        firstNameField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        firstNameField.getStyle().set("max-width", "100%");
        firstNameField.addValueChangeListener(
                s -> {
                    employeeFilter.setFirstName(s.getValue());
                    grid.getDataProvider().refreshAll();
                    grid.refreshPaginator();
                }
        );

        patronymicNameField.setConverters(stringToStringWithNullValueConverter);
        patronymicNameField.setPlaceholder("Отчество");
        patronymicNameField.setClearButtonVisible(true);
        patronymicNameField.setWidthFull();
        patronymicNameField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        patronymicNameField.getStyle().set("max-width", "100%");
        patronymicNameField.addValueChangeListener(
                s -> {
                    employeeFilter.setPatronymicName(s.getValue());
                    grid.getDataProvider().refreshAll();
                    grid.refreshPaginator();
                }
        );

        if (!fromEmployer) {
            employerField.setPlaceholder("Работодатель");
            employerField.setItemLabelGenerator(Employer::getName);
            employerField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
            employerField.setDataProvider(
                    (employer, filterString) ->
                            employer.getName().toLowerCase().startsWith(filterString.toLowerCase()),
                    DataProvider.fromStream(
                            employerService.findAll(
                                    SearchEmployerRequest.newBuilder()
                                            .build()
                            ).stream()
                    ));

            employerField.addValueChangeListener(e -> {
                employeeFilter.setEmployerId(e.getValue() != null ? e.getValue().getId() : null);
                grid.getDataProvider().refreshAll();
                grid.refreshPaginator();
            });
        }


        updatedAtField.setPlaceholder("Дата обновления");
        updatedAtField.setClearButtonVisible(true);
        updatedAtField.setWidthFull();
        updatedAtField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        updatedAtField.getStyle().set("max-width", "100%");
        updatedAtField.addValueChangeListener(
                e -> {
                    //TODO сделать фильтрацию
//                    employeeFilter.setUpdatedAt(e.getValue().atStartOfDay());
//                    grid.getDataProvider().refreshAll();
//                    grid.refreshPaginator();
                });

        createdAtField.setPlaceholder("Дата создания");
        createdAtField.setClearButtonVisible(true);
        createdAtField.setWidthFull();
        createdAtField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        createdAtField.getStyle().set("max-width", "100%");
        createdAtField.addValueChangeListener(
                e -> {
                    //TODO сделать фильтрацию
//                    employeeFilter.setCreatedAt(e.getValue().atStartOfDay());
//                    grid.getDataProvider().refreshAll();
//                    grid.refreshPaginator();
                });
    }

    private void initDataProvider() {
        this.dataProvider = new CallbackDataProvider<Employee, Employee>(
                query -> employeeService.find(query, PAGE_SIZE).stream(),
                query -> employeeService.getTotalCount(query))
                .withConfigurableFilter();

        this.employeeFilter = Employee.builder()
                .status(null)
                .build();
        this.dataProvider.setFilter(this.employeeFilter);
    }

    private Grid<Employee> createGrid() {
        grid = new PaginatedGrid<>();
        grid.setPageSize(PAGE_SIZE);
        grid.setPaginatorSize(2);
        grid.setDataProvider(dataProvider);
        grid.setHeightFull();

        ComponentRenderer<Button, Employee> actionRenderer = new ComponentRenderer<>(
                employee -> {
                    Button editButton = UIUtils.createButton(VaadinIcon.EDIT,
                            ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_SMALL);
                    editButton.addClassName(IconSize.XS.getClassName());
                    editButton.addClickListener(event -> toViewPage(employee));
                    return editButton;
                }
        );
        ComponentRenderer<Badge, Employee> badgeRenderer = new ComponentRenderer<>(
                employee -> {
                    EmployeeStatus status = employee.getStatus();
                    Badge badge = new Badge(status.getDescription(), employee.getStatusTheme());
                    return badge;
                }
        );

        Grid.Column<Employee> actionColumn = grid.addColumn(actionRenderer)
                .setFrozen(true)
                .setFlexGrow(0)
                .setWidth("100px")
                .setHeader("Действие")
                .setResizable(true);

        Grid.Column<Employee> idColumn = grid.addColumn(Employee::getId)
                .setAutoWidth(true)
                .setWidth("100px")
                .setHeader("ID")
                .setSortable(true)
                .setComparator(Employee::getId)
                .setResizable(true);
        idColumn.setVisible(false);

        Grid.Column<Employee> firstNameColumn = grid.addColumn(Employee::getFirstName)
                .setAutoWidth(true)
                .setComparator(Employee::getFirstName)
                .setHeader("Имя")
                .setResizable(true);
        firstNameColumn.setVisible(true);

        Grid.Column<Employee> lastNameColumn = grid.addColumn(Employee::getLastName)
                .setAutoWidth(true)
                .setComparator(Employee::getLastName)
                .setHeader("Фамилия")
                .setResizable(true);
        lastNameColumn.setVisible(false);

        Grid.Column<Employee> employerNameColumn = null;
        if (!fromEmployer) {
            employerNameColumn = grid.addColumn(Employee::getEmployerName)
                    .setAutoWidth(true)
                    .setComparator(Employee::getEmployerName)
                    .setHeader("Работодатель")
                    .setResizable(true);
        }

        Grid.Column<Employee> statusColumn = grid.addColumn(badgeRenderer)
                .setAutoWidth(true)
                .setComparator(Employee::getStatus)
                .setHeader("Статус")
                .setResizable(true);

        Grid.Column<Employee> updatedAtColumn = grid.addColumn(new LocalDateTimeRenderer<>(Employee::getUpdatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Employee::getUpdatedAt)
                .setResizable(true)
                .setHeader("Дата обновления");

        Grid.Column<Employee> createdAtColumn = grid.addColumn(new LocalDateTimeRenderer<>(Employee::getCreatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Employee::getCreatedAt)
                .setResizable(true)
                .setHeader("Дата создания");


        Button menuButton = new Button();
        menuButton.setIcon(VaadinIcon.ELLIPSIS_DOTS_H.create());
        menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ColumnToggleContextMenu columnToggleContextMenu = new ColumnToggleContextMenu(
                menuButton);
        columnToggleContextMenu.addColumnToggleItem("id", idColumn);
        columnToggleContextMenu.addColumnToggleItem("Имя", firstNameColumn);
        columnToggleContextMenu.addColumnToggleItem("Фамилия", lastNameColumn);
        columnToggleContextMenu.addColumnToggleItem("Статус", statusColumn);
        columnToggleContextMenu.addColumnToggleItem("Дата обновления", updatedAtColumn);
        columnToggleContextMenu.addColumnToggleItem("Дата создания", createdAtColumn);

        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();

        headerRow.getCell(actionColumn).setComponent(menuButton);
        headerRow.getCell(idColumn).setComponent(idField);
        headerRow.getCell(statusColumn).setComponent(statusField);
        headerRow.getCell(firstNameColumn).setComponent(firstNameField);
        headerRow.getCell(lastNameColumn).setComponent(lastNameNameField);
        headerRow.getCell(updatedAtColumn).setComponent(updatedAtField);
        headerRow.getCell(createdAtColumn).setComponent(createdAtField);

        if (employerNameColumn != null) {
            headerRow.getCell(employerNameColumn).setComponent(employerField);
        }

        return grid;
    }

    private void toViewPage(Employee employee) {
        Map<String, String> params = Map.of(
                "employeeId", employee.getId(),
                "backToEmployerForm", String.valueOf(fromEmployer)
        );
        UI.getCurrent().navigate(EmployeeView.ROUTE,
                QueryParameters.simple(
                        params
                )
        );
    }

    public void withFilter(Employee employeeFilter) {
        this.employeeFilter = employeeFilter;

        binder.setBean(employeeFilter);
        binder.bindInstanceFields(this);

        dataProvider.setFilter(employeeFilter);
        grid.getDataProvider().refreshAll();
    }

}
