package tech.inno.odp.ui.views.employee.form;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import tech.inno.odp.backend.data.containers.Employee;
import tech.inno.odp.backend.data.enums.EmployeeStatus;
import tech.inno.odp.ui.components.field.CustomTextField;
import tech.inno.odp.ui.util.LumoStyles;
import tech.inno.odp.ui.util.converter.StringToLocalDateTimeConverter;
import tech.inno.odp.ui.util.converter.StringToStringWithNullValueConverter;

import javax.annotation.PostConstruct;

@SpringComponent
@UIScope
public class EmployeeSettingsForm extends VerticalLayout {

    public static final String ID = "employeeSettingsForm";

    private Employee employee;

    @Getter
    private BeanValidationBinder<Employee> binder;

    @PropertyId("id")
    private CustomTextField idField = new CustomTextField("ID");
    @PropertyId("employerId")
    private CustomTextField employerIdField = new CustomTextField("EmployerId");
    @PropertyId("status")
    private ComboBox<EmployeeStatus> statusField = new ComboBox("Статус");
    @PropertyId("phone")
    private TextField phoneField = new TextField("Телефон");
    @PropertyId("firstName")
    private TextField firstNameField = new TextField("Имя");
    @PropertyId("lastName")
    private TextField lastNameField = new TextField("Фамилия");

    @PropertyId("updatedAt")
    private TextField updatedAtField = new TextField("Дата обновления");
    @PropertyId("createdAt")
    private TextField createdAtField = new TextField("Дата создания");

    @PostConstruct
    public void init() {
        setId(ID);

        initFields();
        StringToLocalDateTimeConverter stringToLocalDateTimeConverter = new StringToLocalDateTimeConverter();

        this.binder = new BeanValidationBinder<>(Employee.class);
        this.binder.forField(updatedAtField)
                .withConverter(stringToLocalDateTimeConverter)
                .bind(Employee::getUpdatedAt, Employee::setUpdatedAt);
        this.binder.forField(createdAtField)
                .withConverter(stringToLocalDateTimeConverter)
                .bind(Employee::getCreatedAt, Employee::setCreatedAt);
        add(createForm());
    }

    private void initFields() {
        idField.setReadOnly(true);
        idField.setConverters(new StringToStringWithNullValueConverter());

        employerIdField.setReadOnly(true);
        employerIdField.setConverters(new StringToStringWithNullValueConverter());

        updatedAtField.setReadOnly(true);
        createdAtField.setReadOnly(true);

        statusField.setItems(EmployeeStatus.values());
        statusField.setItemLabelGenerator(EmployeeStatus::getDescription);

        firstNameField.setReadOnly(true);
        lastNameField.setReadOnly(true);
    }

    public FormLayout createForm() {
        FormLayout formLayout = new FormLayout();

        formLayout.add(idField);
        formLayout.setColspan(idField, 2);

        formLayout.add(firstNameField);
        formLayout.add(lastNameField);

        formLayout.add(updatedAtField);
        formLayout.add(createdAtField);

        formLayout.add(phoneField);
        formLayout.add(statusField);


        formLayout.addClassNames(
                LumoStyles.Padding.Bottom.XS,
                LumoStyles.Padding.Horizontal.XS,
                LumoStyles.Padding.Top.XS);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("600px", 2,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("1024px", 2,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP));
        return formLayout;
    }

    public void withBean(Employee employee) {
        this.employee = employee;
        this.binder.removeBean();
        this.binder.setBean(this.employee);
        this.binder.bindInstanceFields(this);
    }
}
