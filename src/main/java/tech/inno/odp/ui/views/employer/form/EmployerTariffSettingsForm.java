package tech.inno.odp.ui.views.employer.form;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import lombok.Getter;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.data.containers.Tariff;
import tech.inno.odp.backend.data.enums.CommissionPayer;
import tech.inno.odp.backend.data.enums.PaymentGatewayProvider;
import tech.inno.odp.backend.data.enums.SpecTariffCondition;
import tech.inno.odp.backend.data.enums.TariffStatus;
import tech.inno.odp.ui.components.field.CustomTextField;
import tech.inno.odp.ui.util.LumoStyles;
import tech.inno.odp.ui.util.converter.BigDecimalToLongConverter;
import tech.inno.odp.ui.util.converter.StringToLocalDateTimeConverter;
import tech.inno.odp.ui.util.converter.StringToStringWithNullValueConverter;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class EmployerTariffSettingsForm extends VerticalLayout {

    public static final String ID = "employerTariffSettingsForm";

    @Getter
    private BeanValidationBinder<Tariff> binder;

    @PropertyId("paymentProvider")
    private ComboBox<PaymentGatewayProvider> paymentProviderField = new ComboBox<>("Платежный шлюз");
    @PropertyId("status")
    private ComboBox<TariffStatus> tariffStatusField = new ComboBox<>("Статус");
    @PropertyId("id")
    private CustomTextField idField = new CustomTextField("Идентификатор тарифа");
    @PropertyId("updatedAt")
    private TextField updatedAtField = new TextField("Дата обновления");
    @PropertyId("createdAt")
    private TextField createdAtField = new TextField("Дата создания");
    @PropertyId("withdrawalPercentage")
    private NumberField withdrawalPercentageField = new NumberField("Процент выдачи");
    @PropertyId("commissionAmount")
    private BigDecimalField commissionAmountField = new BigDecimalField("Размер комиссии, руб.");
    @PropertyId("commissionPayer")
    private ComboBox<CommissionPayer> commissionPayerField = new ComboBox<>("Плательщик комиссии");
    @PropertyId("minAmount")
    private BigDecimalField minAmountField = new BigDecimalField("Минимальная сумма платежа, руб.");
    @PropertyId("maxAmount")
    private BigDecimalField maxAmountField = new BigDecimalField("Максимальная сумма платежа, руб.");
    @PropertyId("maxMonthlyEmployerTurnover")
    private BigDecimalField maxMonthlyEmployerTurnoverField =
            new BigDecimalField("Месячный лимит работодателя, руб.");
    @PropertyId("specTariffCommissionAmount")
    private BigDecimalField specTariffCommissionAmountField =
            new BigDecimalField("Размер комиссии по спецтарифу, руб.");
    @PropertyId("specTariffCondition")
    private ComboBox<SpecTariffCondition> specTariffConditionField =
            new ComboBox<>("Условия срабатывания спецтарифа");
    @PropertyId("preferentialPaymentDays")
    private IntegerField preferentialPaymentDaysField = new IntegerField("Количество льготных дней");
    @PropertyId("preferentialPaymentCount")
    private IntegerField preferentialPaymentCountField = new IntegerField("Количество льготных платежей");

    private Set<Tariff> tariffs;

    public void init() {
        setId(ID);
        initFields();
        bindForField();
        add(createForm());
    }

    private void bindForField() {
        BigDecimalToLongConverter bigDecimalToLongConverter = new BigDecimalToLongConverter();
        StringToLocalDateTimeConverter stringToLocalDateTimeConverter = new StringToLocalDateTimeConverter();

        this.binder = new BeanValidationBinder<>(Tariff.class);
        this.binder.forField(updatedAtField)
                .withConverter(stringToLocalDateTimeConverter)
                .bind(Tariff::getUpdatedAt, Tariff::setUpdatedAt);
        this.binder.forField(createdAtField)
                .withConverter(stringToLocalDateTimeConverter)
                .bind(Tariff::getCreatedAt, Tariff::setCreatedAt);
        this.binder.forField(commissionAmountField)
                .withConverter(bigDecimalToLongConverter)
                .bind("commissionAmount");
        this.binder.forField(specTariffCommissionAmountField)
                .withConverter(bigDecimalToLongConverter)
                .bind("specTariffCommissionAmount");
        this.binder.forField(minAmountField)
                .withConverter(bigDecimalToLongConverter)
                .bind("minAmount");
        this.binder.forField(maxAmountField)
                .withConverter(bigDecimalToLongConverter)
                .bind("maxAmount");
        this.binder.forField(maxMonthlyEmployerTurnoverField)
                .withConverter(bigDecimalToLongConverter)
                .bind("maxMonthlyEmployerTurnover");
        this.binder.forField(tariffStatusField)
                .bind(Tariff::getTariffStatus, Tariff::setTariffStatus);
    }

    private void initFields() {
        idField.setConverters(new StringToStringWithNullValueConverter());

        paymentProviderField.setItems(PaymentGatewayProvider.values());
        paymentProviderField.setItemLabelGenerator(PaymentGatewayProvider::getDescription);
        paymentProviderField.setRequired(true);
        paymentProviderField.addValueChangeListener(event -> {
            bindForField();
            bindBean(getTariff(event.getValue()));
        });

        tariffStatusField.setItems(TariffStatus.values());
        tariffStatusField.setItemLabelGenerator(TariffStatus::getDescription);
        tariffStatusField.setRequired(true);

        idField.setReadOnly(true);
        updatedAtField.setReadOnly(true);
        createdAtField.setReadOnly(true);

        withdrawalPercentageField.setMax(100.0);
        withdrawalPercentageField.setMin(0.0);
        withdrawalPercentageField.setRequiredIndicatorVisible(true);
        withdrawalPercentageField.setWidthFull();

        commissionPayerField.setItems(CommissionPayer.values());
        commissionPayerField.setItemLabelGenerator(CommissionPayer::getDescription);
        commissionPayerField.setRequired(true);
        commissionPayerField.setWidthFull();

        specTariffConditionField.setItems(SpecTariffCondition.values());
        specTariffConditionField.setItemLabelGenerator(SpecTariffCondition::getDescription);
        specTariffConditionField.setRequired(true);
        specTariffConditionField.setWidthFull();

        commissionAmountField.setWidthFull();
        minAmountField.setWidthFull();
        maxAmountField.setWidthFull();
        maxMonthlyEmployerTurnoverField.setWidthFull();
        specTariffCommissionAmountField.setWidthFull();
        preferentialPaymentDaysField.setWidthFull();
        preferentialPaymentCountField.setWidthFull();

        setFieldsReadOnly(true);
    }

    private VerticalLayout createForm() {
        Hr hr = new Hr();
        VerticalLayout rightFormLayout = new VerticalLayout();
        FormLayout leftFormLayout = new FormLayout();
        FormLayout topFormLayout = new FormLayout();
        HorizontalLayout bottomHorizontalLayout = new HorizontalLayout(leftFormLayout, rightFormLayout);
        bottomHorizontalLayout.setSpacing(false);
        bottomHorizontalLayout.getThemeList().add("spacing-xl");

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(topFormLayout, hr, bottomHorizontalLayout);

        paymentProviderField.setMaxWidth("20%");
        topFormLayout.add(paymentProviderField);

        leftFormLayout.add(tariffStatusField);
        leftFormLayout.add(idField);
        leftFormLayout.add(updatedAtField);
        leftFormLayout.add(createdAtField);

        rightFormLayout.add(withdrawalPercentageField);
        rightFormLayout.add(commissionAmountField);
        rightFormLayout.add(commissionPayerField);
        rightFormLayout.add(minAmountField);
        rightFormLayout.add(maxAmountField);
        rightFormLayout.add(maxMonthlyEmployerTurnoverField);
        rightFormLayout.add(specTariffCommissionAmountField);
        rightFormLayout.add(specTariffConditionField);
        rightFormLayout.add(preferentialPaymentDaysField);
        rightFormLayout.add(preferentialPaymentCountField);

        verticalLayout.addClassNames(LumoStyles.Padding.Bottom.L,
                LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);

        return verticalLayout;
    }

    public void withBean(@NotNull final Employer employer) {
        tariffs = employer.getTariffs();
        PaymentGatewayProvider currentPaymentProvider = employer.getPaymentProvider();
        bindBean(getTariff(currentPaymentProvider));
    }

    private Tariff getTariff(PaymentGatewayProvider currentPaymentProvider) {
        Tariff tariff = Tariff.builder().paymentProvider(currentPaymentProvider).build();
        if (currentPaymentProvider == PaymentGatewayProvider.NONE_PAYMENT_PROVIDER) {
            return tariff;
        }

        if (tariffs != null) {
            Optional<Tariff> tariffOptional = tariffs.stream()
                    .filter(t -> t.getPaymentProvider() == (currentPaymentProvider))
                    .findAny();
            if (tariffOptional.isPresent()) {
                return tariffOptional.get();
            }
        } else {
            tariffs = new HashSet<>();
        }
        tariffs.add(tariff);
        return tariff;
    }

    private void bindBean(Tariff tariff) {
        this.binder.setBean(tariff);
        this.binder.bindInstanceFields(this);
    }

    public void setFieldsReadOnly(boolean flag) {
        tariffStatusField.setReadOnly(flag);
        withdrawalPercentageField.setReadOnly(flag);
        commissionAmountField.setReadOnly(flag);
        commissionPayerField.setReadOnly(flag);
        minAmountField.setReadOnly(flag);
        maxAmountField.setReadOnly(flag);
        maxMonthlyEmployerTurnoverField.setReadOnly(flag);
        specTariffCommissionAmountField.setReadOnly(flag);
        specTariffConditionField.setReadOnly(flag);
        preferentialPaymentCountField.setReadOnly(flag);
        preferentialPaymentDaysField.setReadOnly(flag);
    }
}
