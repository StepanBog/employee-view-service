package tech.inno.odp.ui.views.employer.form;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.data.enums.CommissionPayer;
import tech.inno.odp.backend.data.enums.EmployerStatus;
import tech.inno.odp.backend.data.enums.PaymentGatewayProvider;
import tech.inno.odp.ui.components.field.CustomTextField;
import tech.inno.odp.ui.util.LumoStyles;
import tech.inno.odp.ui.util.converter.BigDecimalToLongConverter;
import tech.inno.odp.ui.util.converter.StringToLocalDateTimeConverter;
import tech.inno.odp.ui.util.converter.StringToStringWithNullValueConverter;


public class EmployerSettingsForm extends VerticalLayout {

    public static final String ID = "employerSettingsForm";

    @Getter
    private BeanValidationBinder<Employer> binder;

    @PropertyId("id")
    private CustomTextField idField = new CustomTextField("ID");
    @PropertyId("name")
    private TextField nameField = new TextField("Название");
    @PropertyId("email")
    private EmailField emailField = new EmailField("E-mail");
    @PropertyId("status")
    private ComboBox<EmployerStatus> statusField = new ComboBox("Статус");
    @PropertyId("paymentProvider")
    private ComboBox<PaymentGatewayProvider> paymentMethodField = new ComboBox("Платежный шлюз");
    @PropertyId("updatedAt")
    private TextField updatedAtField = new TextField("Дата обновления");
    @PropertyId("createdAt")
    private TextField createdAtField = new TextField("Дата создания");

    public void init() {
        setId(ID);
        initFields();
        StringToLocalDateTimeConverter stringToLocalDateTimeConverter = new StringToLocalDateTimeConverter();

        this.binder = new BeanValidationBinder<>(Employer.class);
        this.binder.forField(updatedAtField)
                .withConverter(stringToLocalDateTimeConverter)
                .bind(Employer::getUpdatedAt, Employer::setUpdatedAt);
        this.binder.forField(createdAtField)
                .withConverter(stringToLocalDateTimeConverter)
                .bind(Employer::getCreatedAt, Employer::setCreatedAt);

        add(createForm());
    }

    private void initFields() {
        idField.setReadOnly(true);
        idField.setConverters(new StringToStringWithNullValueConverter());
        
        nameField.setRequired(true);
        nameField.setRequiredIndicatorVisible(true);

        paymentMethodField.setItems(PaymentGatewayProvider.values());
        paymentMethodField.setItemLabelGenerator(PaymentGatewayProvider::getDescription);
        paymentMethodField.setRequired(true);

        statusField.setItems(EmployerStatus.values());
        statusField.setItemLabelGenerator(EmployerStatus::getDescription);
        statusField.setRequired(true);

        updatedAtField.setReadOnly(true);
        createdAtField.setReadOnly(true);
    }

    public FormLayout createForm() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(idField);
        formLayout.add(updatedAtField);
        formLayout.add(createdAtField);
        formLayout.add(nameField);
        formLayout.add(statusField);
        formLayout.add(emailField);
        formLayout.add(paymentMethodField);

        formLayout.addClassNames(LumoStyles.Padding.Bottom.L,
                LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("600px", 2,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("1024px", 3,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP));
        return formLayout;
    }

    public void withBean(Employer employer) {
        if (StringUtils.isEmpty(employer.getId())) {
            employer.setStatus(EmployerStatus.CREATED);
        }
        this.binder.removeBean();
        this.binder.setBean(employer);
        this.binder.bindInstanceFields(this);
    }
}
