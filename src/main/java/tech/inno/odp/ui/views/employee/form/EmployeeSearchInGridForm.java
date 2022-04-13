package tech.inno.odp.ui.views.employee.form;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.provider.DataProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tech.inno.odp.backend.data.containers.Employee;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.data.enums.EmployeeStatus;
import tech.inno.odp.backend.service.IEmployerService;
import tech.inno.odp.grpc.generated.service.employer.SearchEmployerRequest;
import tech.inno.odp.ui.components.field.CustomTextField;
import tech.inno.odp.ui.util.LumoStyles;
import tech.inno.odp.ui.util.UIUtils;
import tech.inno.odp.ui.util.converter.LocalDateToLocalDateTimeConverter;
import tech.inno.odp.ui.util.converter.StringToStringWithNullValueConverter;
import tech.inno.odp.ui.views.employee.EmployeeGrid;
import tech.inno.odp.ui.views.employee.EmployeeGridWithFilter;

/**
 * @author VKozlov
 */
@RequiredArgsConstructor
public class EmployeeSearchInGridForm extends VerticalLayout {

    public static final String ID = "employeeSearchInGridForm";

    private final IEmployerService employerService;
    private final EmployeeGrid grid;

    @PropertyId("id")
    private CustomTextField idField = new CustomTextField();
    @PropertyId("lastName")
    private CustomTextField lastNameNameField = new CustomTextField();
    @PropertyId("firstName")
    private CustomTextField firstNameField = new CustomTextField();
    @PropertyId("patronymicName")
    private CustomTextField patronymicNameField = new CustomTextField();
    @PropertyId("phone")
    private CustomTextField phoneField = new CustomTextField();
    @PropertyId("status")
    private ComboBox<EmployeeStatus> statusField = new ComboBox();

    private ComboBox<Employer> employerField = new ComboBox<>();

    @PropertyId("updatedAt")
    private DatePicker updatedAtField = new DatePicker();
    @PropertyId("createdAt")
    private DatePicker createdAtField = new DatePicker();

    @Getter
    private BeanValidationBinder<Employee> binder;

    private Employee employeeFilter;

    public void init() {
        setId(ID);

        initFields();

        this.employeeFilter = Employee.builder()
                .status(null)
                .build();

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

        add(createForm());
    }

    private void initFields() {

        StringToStringWithNullValueConverter stringWithNullValueConverter = new StringToStringWithNullValueConverter();
        idField.setConverters(stringWithNullValueConverter);
        idField.setPlaceholder("ID");
        idField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());

        lastNameNameField.setConverters(stringWithNullValueConverter);
        lastNameNameField.setPlaceholder("Фамилия");
        lastNameNameField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());

        firstNameField.setConverters(stringWithNullValueConverter);
        firstNameField.setPlaceholder("Имя");
        firstNameField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());

        patronymicNameField.setConverters(stringWithNullValueConverter);
        patronymicNameField.setPlaceholder("Отчество");
        patronymicNameField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());

        phoneField.setConverters(stringWithNullValueConverter);
        phoneField.setPlaceholder("Телефон");
        phoneField.setPrefixComponent(new Div(new Text("+7")));
        phoneField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());

        statusField.setItems(EmployeeStatus.values());
        statusField.setItemLabelGenerator(EmployeeStatus::getDescription);
        statusField.setPlaceholder("Статус");
        statusField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());

        employerField.setPlaceholder("Работодатель");
        employerField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        employerField.setItemLabelGenerator(Employer::getName);
        ComboBox.ItemFilter<Employer> filter = (employer, filterString) ->
                employer.getName().toLowerCase().startsWith(filterString.toLowerCase());

        employerField.setDataProvider(filter, DataProvider.fromStream(
                employerService.findAll(
                        SearchEmployerRequest.newBuilder()
                                .build()
                ).stream()
        ));
        employerField.addValueChangeListener(
                e -> employeeFilter.setEmployerId(e.getValue() != null ? e.getValue().getId() : null));

        updatedAtField.setPlaceholder("Дата обновления");
        updatedAtField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        
        createdAtField.setPlaceholder("Дата создания");
        createdAtField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
    }

    public FormLayout createForm() {
        FormLayout formLayout = new FormLayout();

        formLayout.add(new Label("Поиск"));

        formLayout.add(idField);
        formLayout.add(lastNameNameField);
        formLayout.add(firstNameField);
        formLayout.add(patronymicNameField);
        formLayout.add(phoneField);
        formLayout.add(statusField);
        formLayout.add(employerField);

        formLayout.add(updatedAtField);
        formLayout.add(createdAtField);

        formLayout.add(createControlButtons());

        formLayout.addClassNames(
                LumoStyles.Padding.Bottom.XS,
                LumoStyles.Padding.Horizontal.XS,
                LumoStyles.Padding.Top.XS);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP)
        );
        return formLayout;
    }

    private HorizontalLayout createControlButtons() {
        final Button search = UIUtils.createPrimaryButton("Поиск");
        search.addClickListener(event -> {
            employeeFilter = binder.getBean();
            grid.withFilter(employeeFilter);
        });

        final Button clear = UIUtils.createTertiaryButton("Очистить");
        clear.addClickListener(event -> {
            employeeFilter = Employee.builder()
                    .status(null)
                    .build();
            binder.setBean(employeeFilter);
            binder.bindInstanceFields(this);
            employerField.setValue(null);
            grid.withFilter(employeeFilter);
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(search, clear);
        buttonLayout.setSpacing(true);
        buttonLayout.addClassName(LumoStyles.Margin.Top.L);
        return buttonLayout;
    }
}
