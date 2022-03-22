package tech.inno.odp.ui.views.employer.form;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;
import lombok.Getter;
import lombok.Setter;
import tech.inno.odp.backend.data.containers.Tariff;
import tech.inno.odp.ui.util.LumoStyles;


public class EmployerTariffSettingsForm extends VerticalLayout {

    @Setter
    private Tariff tariff;

    @Getter
    private BeanValidationBinder<Tariff> binder;

    @PropertyId("enable")
    private Checkbox enableField = new Checkbox("Активировать тарифный план?");
    @PropertyId("zeroCommissionEnable")
    private Checkbox zeroCommissionEnableField = new Checkbox("Включение 0% комиссии");
    @PropertyId("preferentialPaymentDays")
    private IntegerField preferentialPaymentDaysField = new IntegerField("Количество льготных дней");
    @PropertyId("preferentialPaymentCount")
    private IntegerField preferentialPaymentCountField = new IntegerField("Количество льготных платежей");

    public void init() {
        initFields();

        this.binder = new BeanValidationBinder<>(Tariff.class);
        this.binder.setBean(this.tariff);
        this.binder.bindInstanceFields(this);

        add(createForm());
    }

    private void initFields() {

        zeroCommissionEnableField.setReadOnly(!this.tariff.isEnable());
        preferentialPaymentDaysField.setReadOnly(!this.tariff.isEnable());
        preferentialPaymentCountField.setReadOnly(!this.tariff.isEnable());

        enableField.addValueChangeListener(event -> {
            zeroCommissionEnableField.setReadOnly(!event.getValue());
            preferentialPaymentDaysField.setReadOnly(!event.getValue());
            preferentialPaymentCountField.setReadOnly(!event.getValue());
        });
    }

    public FormLayout createForm() {
        FormLayout formLayout = new FormLayout();

        formLayout.add(enableField);
        formLayout.setColspan(enableField, 2);

        Hr hr = new Hr();
        formLayout.add(hr);
        formLayout.setColspan(hr, 2);

        formLayout.add(zeroCommissionEnableField);
        formLayout.setColspan(zeroCommissionEnableField, 2);

        formLayout.add(preferentialPaymentDaysField);
        formLayout.add(preferentialPaymentCountField);

        formLayout.addClassNames(LumoStyles.Padding.Bottom.L,
                LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("600px", 2,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("1024px", 2,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP));
        return formLayout;
    }
}
