package tech.inno.odp.ui.views.employer.form;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.data.enums.EmployerStatus;
import tech.inno.odp.ui.components.field.CustomTextField;
import tech.inno.odp.ui.util.LumoStyles;
import tech.inno.odp.ui.util.UIUtils;
import tech.inno.odp.ui.util.converter.LocalDateToLocalDateTimeConverter;
import tech.inno.odp.ui.util.converter.StringToStringWithNullValueConverter;
import tech.inno.odp.ui.views.employer.EmployerGrid;

/**
 * @author VKozlov
 * Форма поиска работодателя
 */
@RequiredArgsConstructor
public class EmployerSearchInGridForm extends VerticalLayout {

    public static final String ID = "employerSearchInGridForm";

    private final EmployerGrid grid;

    @PropertyId("id")
    private CustomTextField idField = new CustomTextField();
    @PropertyId("name")
    private CustomTextField nameField = new CustomTextField();

    @PropertyId("email")
    private EmailField emailField = new EmailField();
    @PropertyId("status")
    private ComboBox<EmployerStatus> statusField = new ComboBox();
    @PropertyId("updatedAt")
    private DatePicker updatedAtField = new DatePicker();
    @PropertyId("createdAt")
    private DatePicker createdAtField = new DatePicker();

    @Getter
    private BeanValidationBinder<Employer> binder;

    private Employer employerFilter;

    public void init() {
        setId(ID);

        initFields();

        this.employerFilter = Employer.builder()
                .status(null)
                .name(null)
                .commissionPayer(null)
                .paymentProvider(null)
                .build();

        this.binder = new BeanValidationBinder<>(Employer.class);
        this.binder.setBean(this.employerFilter);

        LocalDateToLocalDateTimeConverter localDateTimeConverter = new LocalDateToLocalDateTimeConverter();

        this.binder.forField(updatedAtField)
                .withConverter(localDateTimeConverter)
                .bind(Employer::getUpdatedAt, Employer::setUpdatedAt);
        this.binder.forField(createdAtField)
                .withConverter(localDateTimeConverter)
                .bind(Employer::getCreatedAt, Employer::setCreatedAt);
        this.binder.setValidatorsDisabled(true);
        this.binder.bindInstanceFields(this);

        add(createForm());
    }

    private void initFields() {

        StringToStringWithNullValueConverter stringWithNullValueConverter = new StringToStringWithNullValueConverter();
        idField.setConverters(stringWithNullValueConverter);
        idField.setPlaceholder("ID");
        idField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());

        emailField.setPlaceholder("Email");
        emailField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());

        statusField.setItems(EmployerStatus.values());
        statusField.setItemLabelGenerator(EmployerStatus::getDescription);
        statusField.setPlaceholder("Статус");
        statusField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());

        nameField.setPlaceholder("Работодатель");
        nameField.setConverters(stringWithNullValueConverter);
        nameField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());

        updatedAtField.setPlaceholder("Дата обновления");
        updatedAtField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());

        createdAtField.setPlaceholder("Дата создания");
        createdAtField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
    }

    public FormLayout createForm() {
        FormLayout formLayout = new FormLayout();

        formLayout.add(new Label("Поиск"));

        formLayout.add(idField);
        formLayout.add(emailField);
        formLayout.add(statusField);
        formLayout.add(nameField);

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
            employerFilter = binder.getBean();
            grid.withFilter(employerFilter);
        });

        final Button clear = UIUtils.createTertiaryButton("Очистить");
        clear.addClickListener(event -> {

            this.employerFilter = Employer.builder()
                    .status(null)
                    .name(null)
                    .commissionPayer(null)
                    .paymentProvider(null)
                    .build();
            binder.setBean(employerFilter);
            binder.bindInstanceFields(this);
            grid.withFilter(employerFilter);
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(search, clear);
        buttonLayout.setSpacing(true);
        buttonLayout.addClassName(LumoStyles.Margin.Top.L);
        return buttonLayout;
    }
}
