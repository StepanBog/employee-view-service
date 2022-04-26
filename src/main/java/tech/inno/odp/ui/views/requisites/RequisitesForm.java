package tech.inno.odp.ui.views.requisites;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import lombok.Getter;
import tech.inno.odp.backend.data.containers.Requisites;
import tech.inno.odp.ui.util.LumoStyles;
import tech.inno.odp.ui.util.converter.StringToLocalDateTimeConverter;

import javax.annotation.PostConstruct;

public class RequisitesForm extends VerticalLayout {

    public static final String ID = "requisitesForm";

    @Getter
    private BeanValidationBinder<Requisites> binder;

    @PropertyId("accountNumber")
    private TextField accountNumberField = new TextField("Счет");
    @PropertyId("inn")
    private TextField innField = new TextField("ИНН");
    @PropertyId("kpp")
    private TextField kppField = new TextField("КПП");
    @PropertyId("bik")
    private TextField bikField = new TextField("БИК");
    @PropertyId("bankName")
    private TextField bankNameField = new TextField("Банк");
    @PropertyId("corr")
    private TextField corrField = new TextField("Корсчет");
    @PropertyId("snils")
    private TextField snilsField = new TextField("Cнилс (без проблелов и разделителей)");
    @PropertyId("passportSeries")
    private TextField passportSeriesField = new TextField("Cерия паспортa");
    @PropertyId("passportNumber")
    private TextField passportNumberField = new TextField("Номер паспорта");
    @PropertyId("firstName")
    private TextField firstNameField = new TextField("Имя");
    @PropertyId("lastName")
    private TextField lastNameField = new TextField("Фамилия");
    @PropertyId("patronymicName")
    private TextField patronymicNameField = new TextField("Отчество");

    @PropertyId("updatedAt")
    private TextField updatedAtField = new TextField("Дата обновления");
    @PropertyId("createdAt")
    private TextField createdAtField = new TextField("Дата создания");

    private FormLayout fioLayout = new FormLayout();
    private FormLayout passportLayout = new FormLayout();
    private FormLayout accountLayout = new FormLayout();


    @PostConstruct
    public void init() {
        setId(ID);

        initFields();

        StringToLocalDateTimeConverter stringToLocalDateTimeConverter = new StringToLocalDateTimeConverter();

        this.binder = new BeanValidationBinder<>(Requisites.class);
        this.binder.forField(updatedAtField)
                .withConverter(stringToLocalDateTimeConverter)
                .bind(Requisites::getUpdatedAt, Requisites::setUpdatedAt);
        this.binder.forField(createdAtField)
                .withConverter(stringToLocalDateTimeConverter)
                .bind(Requisites::getCreatedAt, Requisites::setCreatedAt);
        createForm();
    }

    private void initFields() {
        updatedAtField.setReadOnly(true);
        createdAtField.setReadOnly(true);
    }

    public void createForm() {
        FormLayout dataLayout = new FormLayout();
        dataLayout.add(updatedAtField);
        dataLayout.add(createdAtField);

        dataLayout.addClassNames(LumoStyles.Padding.Bottom.XS,
                LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.XS);
        dataLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("600px", 2,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("1024px", 2,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP));

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setPadding(false);
        verticalLayout.setMargin(false);
        verticalLayout.setSpacing(false);

        accountNumberField.setRequired(true);

        accountLayout.add(accountNumberField);
        accountLayout.add(bankNameField);
        accountLayout.add(snilsField);
        accountLayout.add(innField);
        accountLayout.add(kppField);
        accountLayout.add(bikField);
        accountLayout.add(corrField);

        accountLayout.addClassNames(LumoStyles.Padding.Bottom.XS,
                LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.XS);
        accountLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("600px", 2,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("1024px", 2,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP));


        fioLayout.add(firstNameField);
        fioLayout.add(lastNameField);
        fioLayout.add(patronymicNameField);

        fioLayout.addClassNames(LumoStyles.Padding.Bottom.XS,
                LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.XS);
        fioLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("600px", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("1024px", 3,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP));


        passportLayout.add(passportSeriesField);
        passportLayout.add(passportNumberField);
        passportLayout.addClassNames(LumoStyles.Padding.Bottom.XS,
                LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.XS);
        passportLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("600px", 2,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("1024px", 2,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP));


        verticalLayout.add(dataLayout);
        verticalLayout.add(fioLayout);
        verticalLayout.add(new Hr());
        verticalLayout.add(passportLayout);
        verticalLayout.add(new Hr());
        verticalLayout.add(accountLayout);

        add(verticalLayout);
    }

    public void withBean(Requisites requisites) {
        this.binder.removeBean();
        this.binder.setBean(requisites);
        this.binder.bindInstanceFields(this);
    }
}
