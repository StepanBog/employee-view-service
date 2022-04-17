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
    protected final int PAGE_SIZE = 15;

    @Setter
    protected IEmployeeService employeeService;
    @Setter
    protected IEmployerService employerService;
    @Setter
    protected boolean fromEmployer = false;

    private ComboBox<Employer> employerField = new ComboBox<>();

    @Getter
    protected Employee employeeFilter;

    protected PaginatedGrid<Employee> grid;
    protected ConfigurableFilterDataProvider<Employee, Void, Employee> dataProvider;

    public void init() {
        setId(ID);
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

        Grid.Column<Employee> patronymicNameColumn = grid.addColumn(Employee::getPatronymicName)
                .setAutoWidth(true)
                .setComparator(Employee::getPatronymicName)
                .setHeader("Отчество")
                .setResizable(true);
        patronymicNameColumn.setVisible(false);

        Grid.Column<Employee> phoneColumn = grid.addColumn(Employee::getPhone)
                .setAutoWidth(true)
                .setComparator(Employee::getPhone)
                .setHeader("Телефон")
                .setResizable(true);
        phoneColumn.setVisible(false);

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
        columnToggleContextMenu.addColumnToggleItem("Отчество", patronymicNameColumn);
        columnToggleContextMenu.addColumnToggleItem("Телефон", phoneColumn);
        columnToggleContextMenu.addColumnToggleItem("Статус", statusColumn);
        columnToggleContextMenu.addColumnToggleItem("Дата обновления", updatedAtColumn);
        columnToggleContextMenu.addColumnToggleItem("Дата создания", createdAtColumn);

        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.getHeaderRows().get(0);

        headerRow.getCell(actionColumn).setComponent(menuButton);
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
        dataProvider.setFilter(employeeFilter);
        grid.getDataProvider().refreshAll();
    }

}
