package tech.inno.odp.ui.views.user.form;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.provider.DataProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.data.containers.User;
import tech.inno.odp.backend.data.enums.UserRoleName;
import tech.inno.odp.backend.service.IEmployerService;
import tech.inno.odp.grpc.generated.service.employer.SearchEmployerRequest;
import tech.inno.odp.ui.components.field.CustomTextField;
import tech.inno.odp.ui.util.LumoStyles;
import tech.inno.odp.ui.util.UIUtils;
import tech.inno.odp.ui.util.converter.LocalDateToLocalDateTimeConverter;
import tech.inno.odp.ui.util.converter.StringToStringWithNullValueConverter;
import tech.inno.odp.ui.views.user.UserGrid;

import java.util.Collections;
import java.util.List;

/**
 * @author VKozlov
 */
@RequiredArgsConstructor
public class UserSearchInGridForm extends VerticalLayout {

    public static final String ID = "UserSearchForm";

    private final IEmployerService employerService;
    private final UserGrid grid;

    @PropertyId("id")
    private CustomTextField idField = new CustomTextField();
    @PropertyId("username")
    private CustomTextField usernameField = new CustomTextField();
    @PropertyId("employeeId")
    private CustomTextField employeeIdField = new CustomTextField();

    private ComboBox<UserRoleName> userRoleNameField = new ComboBox<>();

    private ComboBox<Employer> employerField = new ComboBox<>();

    @PropertyId("updatedAt")
    private DatePicker updatedAtField = new DatePicker();
    @PropertyId("createdAt")
    private DatePicker createdAtField = new DatePicker();

    @Getter
    private BeanValidationBinder<User> binder;

    private User userFilter;

    public void init() {
        setId(ID);

        initFields();

        this.userFilter = User.builder()
                .roleNames(null)
                .build();


        this.binder = new BeanValidationBinder<>(User.class);
        this.binder.setBean(this.userFilter);

        LocalDateToLocalDateTimeConverter localDateTimeConverter = new LocalDateToLocalDateTimeConverter();
        this.binder.forField(updatedAtField)
                .withConverter(localDateTimeConverter)
                .bind(User::getUpdatedAt, User::setUpdatedAt);
        this.binder.forField(createdAtField)
                .withConverter(localDateTimeConverter)
                .bind(User::getCreatedAt, User::setCreatedAt);
        this.binder.bindInstanceFields(this);

        add(createForm());
    }

    private void initFields() {
        StringToStringWithNullValueConverter stringToStringWithNullValueConverter = new StringToStringWithNullValueConverter();

        idField.setPlaceholder("ID");
        idField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        idField.setConverters(stringToStringWithNullValueConverter);

        usernameField.setPlaceholder("Username");
        usernameField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        usernameField.setConverters(stringToStringWithNullValueConverter);

        userRoleNameField.setItems(UserRoleName.values());
        userRoleNameField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        userRoleNameField.setItemLabelGenerator(UserRoleName::getDescription);
        userRoleNameField.setPlaceholder("Роль");

        employeeIdField.setPlaceholder("Идентификатор работника");
        employeeIdField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        employeeIdField.setConverters(stringToStringWithNullValueConverter);

        
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
                e -> userFilter.setEmployerId(e.getValue() != null ? e.getValue().getId() : null));
        employerField.setPlaceholder("Работодатель");
        employerField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());

        updatedAtField.setPlaceholder("Дата обновления");
        updatedAtField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());

        createdAtField.setPlaceholder("Дата создания");
        createdAtField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
    }

    public FormLayout createForm() {
        FormLayout formLayout = new FormLayout();

        formLayout.add(new Label("Поиск"));

        formLayout.add(idField);
        formLayout.add(employeeIdField);
        formLayout.add(usernameField);
        formLayout.add(employerField);
        formLayout.add(userRoleNameField);

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
            userFilter = binder.getBean();
            if (userRoleNameField.getValue() != null) {
                userFilter.setRoleNames(List.of(userRoleNameField.getValue()));
            }
            grid.withFilter(userFilter);
        });

        final Button clear = UIUtils.createTertiaryButton("Очистить");
        clear.addClickListener(event -> {
            userFilter = User.builder()
                    .roleNames(Collections.emptyList())
                    .build();
            binder.setBean(userFilter);
            binder.bindInstanceFields(this);
            employerField.setValue(null);
            userRoleNameField.setValue(null);
            grid.withFilter(userFilter);
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(search, clear);
        buttonLayout.setSpacing(true);
        buttonLayout.addClassName(LumoStyles.Margin.Top.L);
        return buttonLayout;
    }
}
