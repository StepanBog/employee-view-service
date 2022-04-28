package tech.inno.odp.ui.views.employer.form;

import com.vaadin.flow.component.Component;
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
import tech.inno.odp.grpc.generated.service.employee.SearchEmployeeRequest;
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

    @PropertyId("managerFio")
    private TextField managerFioField = new TextField("Менеджер");
    @PropertyId("managerPhone")
    private TextField managerPhoneField = new TextField("Телефон менеджера");
    @PropertyId("managerEmail")
    private TextField managerEmailField = new TextField("Email");
    @PropertyId("employerContactFio")
    private TextField employerContactFioField = new TextField("Контактное лицо работодателя");
    @PropertyId("employerContactPhone")
    private TextField employerContactPhoneField = new TextField("Телефон контактного лица");
    @PropertyId("employerContactEmail")
    private TextField employerContactEmailField = new TextField("Email");

    @Setter
    private IEmployeeService employeeService;

    public void init() {
        setId(ID);
        initFields();
        StringToLocalDateTimeConverter stringToLocalDateTimeConverter = new StringToLocalDateTimeConverter();

        binder = new BeanValidationBinder<>(Employer.class);
        binder.forField(updatedAtField)
                .withConverter(stringToLocalDateTimeConverter)
                .bind(Employer::getUpdatedAt, Employer::setUpdatedAt);
        binder.forField(createdAtField)
                .withConverter(stringToLocalDateTimeConverter)
                .bind(Employer::getCreatedAt, Employer::setCreatedAt);
        binder.forField(employeesField)
                .bind((ValueProvider<Employer, String>) employer ->
                        employer.getId() == null ? "" :
                        String.valueOf(employeeService.find(
                                        SearchEmployeeRequest.newBuilder()
                                                .setEmployerId(employer.getId())
                                                .build())
                                        .getEmployeesList().size()), null);
        binder.forField(activeEmployeesField)
                .bind((ValueProvider<Employer, String>) employer ->
                        employer.getId() == null ? "" :
                        String.valueOf(employeeService.find(
                                        SearchEmployeeRequest.newBuilder()
                                                .setEmployerId(employer.getId())
                                                .setStatus(EmployeeStatus.ENABLED)
                                                .build())
                                        .getEmployeesList().size()), null);
        binder.forField(innField)
                .withValidator(s -> s.length() <= 12, "Количество цифр должно быть меньше 13")
                .withValidator(s -> Pattern.compile("^[0-9]+$").matcher(s).find(), "Только цифры")
                .bind(Employer::getInn, Employer::setInn);
        binder.forField(kppField)
                .withValidator(s -> Pattern.compile("^[0-9]+$").matcher(s).find(), "Только цифры")
                .bind(Employer::getKpp, Employer::setKpp);
        add(createForm());
    }

    private void initFields() {
        nameField.setRequired(true);
        nameField.setRequiredIndicatorVisible(true);

        idField.setConverters(new StringToStringWithNullValueConverter());

        statusField.setItems(EmployerStatus.values());
        statusField.setItemLabelGenerator(EmployerStatus::getDescription);
        statusField.setRequired(true);

        paymentMethodField.setItems(PaymentGatewayProvider.values());
        paymentMethodField.setItemLabelGenerator(PaymentGatewayProvider::getDescription);
        paymentMethodField.setRequired(true);

        budgetOrgField.setItems(BudgetOrganization.values());
        budgetOrgField.setItemLabelGenerator(BudgetOrganization::getDescription);
        budgetOrgField.setRequired(true);

        managerEmailField.setPreventInvalidInput(true);

        setFieldsReadOnly(true);
    }

    private Component createForm() {
        FormLayout topRightFormLayout = new FormLayout();
        FormLayout bottomRightFormLayout = new FormLayout();
        FormLayout empty40FormLayout = new FormLayout();
        FormLayout empty30FormLayout = new FormLayout();
        empty40FormLayout.setHeight("40px");
        empty30FormLayout.setHeight("30px");
        VerticalLayout rightVerticalLayout = new VerticalLayout(
                new Label("Контактные лица"),
                new Hr(),
                topRightFormLayout,
                empty40FormLayout,
                bottomRightFormLayout);
        FormLayout topLeftFormLayout = new FormLayout();
        FormLayout bottomLeftFormLayout = new FormLayout();
        VerticalLayout leftFormLayout = new VerticalLayout(
                topLeftFormLayout,
                empty30FormLayout,
                bottomLeftFormLayout);
        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.add(leftFormLayout, rightVerticalLayout);
        mainLayout.setSpacing(false);
        mainLayout.getThemeList().add("spacing-xl");

        rightVerticalLayout.setMaxWidth("25%");
        leftFormLayout.setMaxWidth("25%");

        topLeftFormLayout.add(nameField);
        topLeftFormLayout.add(idField);
        topLeftFormLayout.add(statusField);

        bottomLeftFormLayout.add(innField);
        bottomLeftFormLayout.add(kppField);
        bottomLeftFormLayout.add(budgetOrgField);
        bottomLeftFormLayout.add(paymentMethodField);
        bottomLeftFormLayout.add(updatedAtField);
        bottomLeftFormLayout.add(createdAtField);
        bottomLeftFormLayout.add(employeesField);
        bottomLeftFormLayout.add(activeEmployeesField);

        topRightFormLayout.add(managerFioField);
        topRightFormLayout.add(managerPhoneField);
        topRightFormLayout.add(managerEmailField);
        bottomRightFormLayout.add(employerContactFioField);
        bottomRightFormLayout.add(employerContactPhoneField);
        bottomRightFormLayout.add(employerContactEmailField);

        mainLayout.addClassNames(LumoStyles.Padding.Bottom.L,
                LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);

        List<FormLayout.ResponsiveStep> responsiveSteps = List.of(
                new FormLayout.ResponsiveStep("0", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("600px", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP));
        topLeftFormLayout.setResponsiveSteps(responsiveSteps);
        bottomLeftFormLayout.setResponsiveSteps(responsiveSteps);
        topRightFormLayout.setResponsiveSteps(responsiveSteps);
        bottomRightFormLayout.setResponsiveSteps(responsiveSteps);
        return mainLayout;
    }

    public void withBean(Employer employer) {
        if (StringUtils.isEmpty(employer.getId())) {
            employer.setStatus(EmployerStatus.CREATED);
        }

        Contact managerContact = setContact(employer, ContactPosition.MANAGER.name());
        Contact employerContact = setContact(employer, ContactPosition.EMPLOYERS_CONTACT.name());

        bindContacts(managerFioField, managerPhoneField, managerEmailField, managerContact);
        bindContacts(employerContactFioField, employerContactPhoneField, employerContactEmailField, employerContact);

        binder.setBean(employer);
        binder.bindInstanceFields(this);
    }

    private Contact setContact(Employer employer, String position) {
        Contact result;
        Set<Contact> contacts = employer.getContacts();
        Optional<Contact> managerOptional = contacts.stream()
                .filter(c -> c.getPosition().name().equals(position)).findAny();
        if (managerOptional.isPresent()) {
            result = managerOptional.get();
        } else {
            result = Contact.builder().position(ContactPosition.MANAGER).build();
            contacts.add(result);
        }
        return result;
    }

    private void bindContacts(TextField fioField, TextField phoneFiled, TextField emailField, Contact contact) {
        binder.forField(fioField).bind(
                (ValueProvider<Employer, String>) employer -> contact.getName(),
                (com.vaadin.flow.data.binder.Setter<Employer, String>) (employer, s) -> contact.setName(s)
        );
        binder.forField(phoneFiled)
                .withValidator(s -> phonePattern.matcher(s).find(), "Некорректный номер телефона")
                .bind(
                (ValueProvider<Employer, String>) employer -> contact.getPhone(),
                (com.vaadin.flow.data.binder.Setter<Employer, String>) (employer, s) -> contact.setPhone(s)
        );

        binder.forField(emailField)
                .withValidator(new EmailValidator("Некоректный адрес электронной почты"))
                .bind(
                (ValueProvider<Employer, String>) employer -> contact.getEmail(),
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
