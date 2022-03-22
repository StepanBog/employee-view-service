package tech.inno.odp.ui.views.employee;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import tech.inno.odp.backend.data.containers.Employee;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.data.enums.EmployeeStatus;
import tech.inno.odp.backend.data.enums.WithDescription;
import tech.inno.odp.backend.service.IEmployeeService;
import tech.inno.odp.ui.components.Badge;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class EmployeeGrid extends VerticalLayout {

    private final int PAGE_SIZE = 50;

    @Setter
    private IEmployeeService employeeService;
    @Setter
    private Employer employer;
    @Setter
    private boolean fromEmployerForm = false;

    private Grid<Employee> grid;
    private ConfigurableFilterDataProvider<Employee, Void, Employee> dataProvider;
    private Employee employeeFilter;

    public void init() {
        setSizeFull();
        add(createContent());
    }

    private Component createContent() {
        initDataProvider();

        VerticalLayout content = new VerticalLayout(
                createGrid()
        );

        content.setBoxSizing(BoxSizing.BORDER_BOX);
        content.setHeightFull();
        content.setPadding(true);
        return content;
    }

    private void initDataProvider() {
        this.dataProvider = new CallbackDataProvider<Employee, Employee>(
                query -> employeeService.find(query, PAGE_SIZE).stream(),
                query -> employeeService.getTotalCount(query))
                .withConfigurableFilter();

        this.employeeFilter = Employee.builder()
                .status(null)
                .employerId(employer.getId())
                .build();
        this.dataProvider.setFilter(this.employeeFilter);
    }

    private Grid<Employee> createGrid() {
        grid = new Grid<>();
        grid.addSelectionListener(event -> event.getFirstSelectedItem().ifPresent(this::toViewPage));
        grid.setPageSize(PAGE_SIZE);
//        grid.setPaginatorSize(2);
        grid.setDataProvider(dataProvider);

        ComponentRenderer<Badge, Employee> badgeRenderer = new ComponentRenderer<>(
                employee -> {
                    EmployeeStatus status = employee.getStatus();
                    Badge badge = new Badge(status.getDescription(), employee.getStatusTheme());
                    return badge;
                }
        );
        Grid.Column<Employee> idColumn = grid.addColumn(Employee::getId)
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setFrozen(true)
                .setHeader("ID");

        Grid.Column<Employee> firstNameColumn = grid.addColumn(Employee::getFirstName)
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setHeader("Имя");

        Grid.Column<Employee> lastNameColumn = grid.addColumn(Employee::getLastName)
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setHeader("Фамилия");

        Grid.Column<Employee> statusColumn = grid.addColumn(badgeRenderer)
                .setWidth("200px")
                .setHeader("Статус");

        Grid.Column<Employee> updatedAtColumn = grid.addColumn(new LocalDateTimeRenderer<>(Employee::getUpdatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Employee::getUpdatedAt)
                .setHeader("Дата обновления");

        Grid.Column<Employee> createdAtColumn = grid.addColumn(new LocalDateTimeRenderer<>(Employee::getCreatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Employee::getCreatedAt)
                .setHeader("Дата создания");


        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();

        headerRow.getCell(idColumn).setComponent(
                createTextFieldFilterHeader("ID", name -> {
                    employeeFilter.setId(StringUtils.isEmpty(name) ? null : name);
                    grid.getDataProvider().refreshAll();
                }));

        headerRow.getCell(statusColumn).setComponent(
                createComboBoxFilterHeader("Статус",
                        Stream.of(EmployeeStatus.values())
                                .collect(Collectors.toList()),
                        s -> {
                            employeeFilter.setStatus(s);
                            grid.getDataProvider().refreshAll();
                        }));

        headerRow.getCell(updatedAtColumn).setComponent(
                createCDataPickerFilterHeader("Дата обновления", name -> {
                    employeeFilter.setUpdatedAt(name.atStartOfDay());
                    grid.getDataProvider().refreshAll();
                }));

        headerRow.getCell(createdAtColumn).setComponent(
                createCDataPickerFilterHeader("Дата создания", name -> {
                    employeeFilter.setCreatedAt(name.atStartOfDay());
                    grid.getDataProvider().refreshAll();
                }));

        return grid;
    }

    private static TextField createTextFieldFilterHeader(String placeHolder,
                                                         Consumer<String> filterChangeConsumer) {
        TextField textField = new TextField();
        textField.setPlaceholder(placeHolder);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        textField.setWidthFull();
        textField.getStyle().set("max-width", "100%");
        textField.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        return textField;
    }

    private static <T extends WithDescription> ComboBox<T> createComboBoxFilterHeader(String placeHolder,
                                                                                      List<T> items,
                                                                                      Consumer<T> filterChangeConsumer) {
        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.setItems(items);
        comboBox.setItemLabelGenerator(T::getDescription);

        comboBox.setPlaceholder(placeHolder);
        comboBox.setClearButtonVisible(true);
        comboBox.setWidthFull();
        comboBox.getStyle().set("max-width", "100%");
        comboBox.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        return comboBox;
    }

    private static DatePicker createCDataPickerFilterHeader(String placeHolder,
                                                            Consumer<LocalDate> filterChangeConsumer) {
        DatePicker datePicker = new DatePicker();

        datePicker.setPlaceholder(placeHolder);
        datePicker.setClearButtonVisible(true);
        datePicker.setWidthFull();
        datePicker.getStyle().set("max-width", "100%");
        datePicker.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        return datePicker;
    }

    private void toViewPage(Employee employee) {
        UI.getCurrent().navigate(EmployeeView.class, employee.getId());
    }
}
