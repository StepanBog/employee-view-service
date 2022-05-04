package tech.inno.odp.ui.views.employer.form;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.function.ValueProvider;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import tech.inno.odp.backend.data.containers.Contact;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.data.enums.BudgetOrganization;
import tech.inno.odp.backend.data.enums.ContactPosition;
import tech.inno.odp.backend.data.enums.EmployerStatus;
import tech.inno.odp.backend.data.enums.PaymentGatewayProvider;
import tech.inno.odp.backend.service.IEmployeeService;
import tech.inno.odp.grpc.generated.common.employee.EmployeeStatus;
import tech.inno.odp.ui.components.field.CustomTextField;
import tech.inno.odp.ui.util.LumoStyles;
import tech.inno.odp.ui.util.converter.StringToLocalDateTimeConverter;
import tech.inno.odp.ui.util.converter.StringToStringWithNullValueConverter;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;


public class EmployerSettingsForm extends VerticalLayout {

    public static final String ID = "employerSettingsForm";

    private Pattern phonePattern = Pattern.compile(
                    "^(\\+7|7|8)?[\\s\\-]?\\(?[489][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$");

    @Getter
    private BeanValidationBinder<Employer> binder;

    @PropertyId("name")
    private TextField nameField = new TextField("Название");
    @PropertyId("id")
    private CustomTextField idField = new CustomTextField("ID");
    @PropertyId("status")
    private ComboBox<EmployerStatus> statusField = new ComboBox<>("Статус");
    @PropertyId("inn")
    private TextField innField = new TextField("ИНН");
    @PropertyId("kpp")
    private TextField kppField = new TextField("КПП");
    @PropertyId("budgetOrg")
    private ComboBox<BudgetOrganization> budgetOrgField = new ComboBox<>("Бюджетная организация");
    @PropertyId("paymentProvider")
    private ComboBox<PaymentGatewayProvider> paymentMethodField = new ComboBox<>("Основной платежный шлюз");
    @PropertyId("updatedAt")
    private TextField updatedAtField = new TextField("Дата обновления");
    @PropertyId("createdAt")
    private TextField createdAtField = new TextField("Дата создания");
    @PropertyId("employees")
    private TextField employeesField = new TextField("Зарегистрировано работников");
    @PropertyId("activeEmployees")
    private TextField activeEmployeesField = new TextField("Активных работников");

    private TextField managerFioField = new TextField("Менеджер");
    private TextField managerPhoneField = new TextField("Телефон менеджера");
    private TextField managerEmailField = new TextField("Email");
    private TextField employerContactFioField = new TextField("Контактное лицо работодателя");
    private TextField employerContactPhoneField = new TextField("Телефон контактного лица");
    private TextField employerContactEmailField = new TextField("Email");

    private final List<FormLayout.ResponsiveStep> responsiveSteps = List.of(
            new FormLayout.ResponsiveStep("0", 1,
                    FormLayout.ResponsiveStep.LabelsPosition.TOP),
            new FormLayout.ResponsiveStep("600px", 1,
                    FormLayout.ResponsiveStep.LabelsPosition.TOP));

    @Setter
    private IEmployeeService employeeService;

    public void init() {
        setId(ID);
        setFieldsReadOnly(true);
        StringToLocalDateTimeConverter stringToLocalDateTimeConverter = new StringToLocalDateTimeConverter();

        binder = new BeanValidationBinder<>(Employer.class);
        binder.forField(updatedAtField)
                .withConverter(stringToLocalDateTimeConverter)
                .bind(Employer::getUpdatedAt, Employer::setUpdatedAt);
        binder.forField(createdAtField)
                .withConverter(stringToLocalDateTimeConverter)
                .bind(Employer::getCreatedAt, Employer::setCreatedAt);
        binder.bind(employeesField, (ValueProvider<Employer, String>) employer ->
                                employer.getId() == null ? "" : Long.toString(
                                        employeeService.countEmployeesByEmployer(employer)), null);
        binder.bind(activeEmployeesField, (ValueProvider<Employer, String>) employer ->
                        employer.getId() == null ? "" : Long.toString(
                                employeeService.countEmployeesByEmployer(employer, EmployeeStatus.ENABLED)
                        ), null);

        add(createForm());
    }

    private HorizontalLayout createForm() {
        VerticalLayout rightVerticalLayout = new VerticalLayout(
                new Label("Контактные лица"),
                new Hr(),
                getTopRightFormLayout(),
                getEmptySpace("40px"),
                getBottomRightFormLayout());
        rightVerticalLayout.setMaxWidth("25%");

        VerticalLayout leftFormLayout = new VerticalLayout(
                getTopLeftFormLayout(),
                getEmptySpace("30px"),
                getBottomLeftFormLayout());
        leftFormLayout.setMaxWidth("25%");

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSpacing(false);
        mainLayout.getThemeList().add("spacing-xl");
        mainLayout.addClassNames(LumoStyles.Padding.Bottom.L,
                LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);

        mainLayout.add(leftFormLayout, rightVerticalLayout);

        return mainLayout;
    }

    private FormLayout getEmptySpace(String size) {
        FormLayout formLayout = new FormLayout();
        formLayout.setHeight(size);
        return formLayout;
    }

    private FormLayout getBottomRightFormLayout() {
        FormLayout formLayout = new FormLayout(
                employerContactFioField,
                employerContactPhoneField,
                employerContactEmailField
        );
        formLayout.setResponsiveSteps(responsiveSteps);
        return formLayout;
    }

    private FormLayout getTopRightFormLayout() {
        managerEmailField.setPreventInvalidInput(true);

        FormLayout formLayout = new FormLayout(
                managerFioField,
                managerPhoneField,
                managerEmailField
        );
        formLayout.setResponsiveSteps(responsiveSteps);
        return formLayout;
    }

    private FormLayout getBottomLeftFormLayout() {
        paymentMethodField.setItems(PaymentGatewayProvider.values());
        paymentMethodField.setItemLabelGenerator(PaymentGatewayProvider::getDescription);
        paymentMethodField.setRequired(true);

        budgetOrgField.setItems(BudgetOrganization.values());
        budgetOrgField.setItemLabelGenerator(BudgetOrganization::getDescription);
        budgetOrgField.setRequired(true);

        FormLayout formLayout = new FormLayout(
                innField,
                kppField,
                budgetOrgField,
                paymentMethodField,
                updatedAtField,
                createdAtField,
                employeesField,
                activeEmployeesField
        );
        formLayout.setResponsiveSteps(responsiveSteps);
        return formLayout;
    }

    private FormLayout getTopLeftFormLayout() {
        nameField.setRequired(true);
        nameField.setRequiredIndicatorVisible(true);

        idField.setConverters(new StringToStringWithNullValueConverter());

        statusField.setItems(EmployerStatus.values());
        statusField.setItemLabelGenerator(EmployerStatus::getDescription);
        statusField.setRequired(true);

        FormLayout formLayout = new FormLayout(
                nameField,
                idField,
                statusField
        );
        formLayout.setResponsiveSteps(responsiveSteps);
        return formLayout;
    }

    public void withBean(Employer employer) {
        if (StringUtils.isEmpty(employer.getId())) {
            employer.setStatus(EmployerStatus.CREATED);
        }

        Contact managerContact = setContact(employer, ContactPosition.MANAGER);
        Contact employerContact = setContact(employer, ContactPosition.EMPLOYERS_CONTACT);

        bindContacts(managerFioField, managerPhoneField, managerEmailField, managerContact);
        bindContacts(employerContactFioField, employerContactPhoneField, employerContactEmailField, employerContact);

        binder.bindInstanceFields(this);
        binder.setBean(employer);
    }

    private Contact setContact(Employer employer, ContactPosition position) {
        Contact result;
        Set<Contact> contacts = employer.getContacts();
        Optional<Contact> managerOptional = contacts.stream()
                .filter(c -> c.getPosition() == position)
                .findAny();
        if (managerOptional.isPresent()) {
            result = managerOptional.get();
        } else {
            result = Contact.builder().position(position).build();
            contacts.add(result);
        }
        return result;
    }

    private void bindContacts(TextField fioField, TextField phoneFiled, TextField emailField, Contact contact) {
        binder.bind(fioField, (ValueProvider<Employer, String>) employer -> contact.getName(),
                (com.vaadin.flow.data.binder.Setter<Employer, String>) (employer, s) -> contact.setName(s)
        );
        binder.forField(phoneFiled)
                .withValidator(s -> phonePattern.matcher(s).find(), "Некорректный номер телефона")
                .bind((ValueProvider<Employer, String>) employer -> contact.getPhone(),
                (com.vaadin.flow.data.binder.Setter<Employer, String>) (employer, s) -> contact.setPhone(s)
        );
        binder.forField(emailField)
                .withValidator(new EmailValidator("Некоректный адрес электронной почты"))
                .bind((ValueProvider<Employer, String>) employer -> contact.getEmail(),
                (com.vaadin.flow.data.binder.Setter<Employer, String>) (employer, s) -> contact.setEmail(s)
        );
    }

    public void setFieldsReadOnly(boolean flag) {
        nameField.setReadOnly(flag);
        statusField.setReadOnly(flag);
        innField.setReadOnly(flag);
        kppField.setReadOnly(flag);
        budgetOrgField.setReadOnly(flag);
        paymentMethodField.setReadOnly(flag);
        managerFioField.setReadOnly(flag);
        managerPhoneField.setReadOnly(flag);
        managerEmailField.setReadOnly(flag);
        employerContactFioField.setReadOnly(flag);
        employerContactPhoneField.setReadOnly(flag);
        employerContactEmailField.setReadOnly(flag);
        idField.setReadOnly(true);
        updatedAtField.setReadOnly(true);
        createdAtField.setReadOnly(true);
    }
}
