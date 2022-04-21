package tech.inno.odp.ui.views.user.form;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tech.inno.odp.backend.data.containers.User;
import tech.inno.odp.backend.data.enums.UserRoleName;
import tech.inno.odp.ui.components.field.CustomTextField;
import tech.inno.odp.ui.util.LumoStyles;
import tech.inno.odp.ui.util.converter.StringToLocalDateTimeConverter;
import tech.inno.odp.ui.util.converter.StringToStringWithNullValueConverter;

import java.util.*;

@RequiredArgsConstructor
public class UserSettingsForm extends VerticalLayout {

    public static final String ID = "userSettingsForm";

    @Getter
    private BeanValidationBinder<User> binder;

    @PropertyId("id")
    private CustomTextField idField = new CustomTextField("ID пользователя");
    @PropertyId("enabled")
    private Checkbox enabledField = new Checkbox("Пользователь доступен");
    @PropertyId("username")
    private TextField usernameField = new TextField("Имя пользователя");
    @PropertyId("password")
    PasswordField passwordField = new PasswordField("Пароль");
    @PropertyId("employerId")
    private CustomTextField employerIdField = new CustomTextField("ID работодателя");
    @PropertyId("employeeId")
    private CustomTextField employeeIdField = new CustomTextField("ID работника");
    @PropertyId("roleNames")
    private CheckboxGroup<UserRoleName> rolesField = new CheckboxGroup<>();
    @PropertyId("updatedAt")
    private TextField updatedAtField = new TextField("Дата обновления");
    @PropertyId("createdAt")
    private TextField createdAtField = new TextField("Дата создания");

    public void init() {
        setId(ID);
        initFields();

        StringToLocalDateTimeConverter stringToLocalDateTimeConverter = new StringToLocalDateTimeConverter();

        this.binder = new BeanValidationBinder<>(User.class);

        this.binder.forField(rolesField)
                .withConverter(new Converter<Set<UserRoleName>, List<UserRoleName>>() {
                    @Override
                    public Result<List<UserRoleName>> convertToModel(Set<UserRoleName> value, ValueContext context) {
                        return value == null ? Result.ok(null) : Result.ok(new ArrayList<>(value));
                    }

                    @Override
                    public Set<UserRoleName> convertToPresentation(List<UserRoleName> value, ValueContext context) {
                        return value == null ? Collections.emptySet() : new HashSet<>(value);
                    }
                })
                .bind(User::getRoleNames, User::setRoleNames);

        this.binder.forField(updatedAtField)
                .withConverter(stringToLocalDateTimeConverter)
                .bind(User::getUpdatedAt, User::setUpdatedAt);
        this.binder.forField(createdAtField)
                .withConverter(stringToLocalDateTimeConverter)
                .bind(User::getCreatedAt, User::setCreatedAt);
        add(createForm());
    }

    private FormLayout createForm() {
        FormLayout formLayout = new FormLayout();

        formLayout.add(idField);
        formLayout.setColspan(idField, 2);
        formLayout.add(employerIdField);
        formLayout.setColspan(employerIdField, 2);
        formLayout.add(employeeIdField);
        formLayout.setColspan(employeeIdField, 2);

        formLayout.add(updatedAtField);
        formLayout.add(createdAtField);

        formLayout.add(usernameField);
        formLayout.add(enabledField);
        formLayout.add(passwordField);

        formLayout.add(rolesField);
        formLayout.setColspan(rolesField, 2);

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

    private void initFields() {
        StringToStringWithNullValueConverter nullValueConverter = new StringToStringWithNullValueConverter();

        idField.setReadOnly(true);
        idField.setConverters(nullValueConverter);

        employerIdField.setReadOnly(true);
        employerIdField.setConverters(nullValueConverter);
        employeeIdField.setReadOnly(true);
        employeeIdField.setConverters(nullValueConverter);

        usernameField.setReadOnly(true);
        enabledField.setReadOnly(true);
        passwordField.setRevealButtonVisible(false);

        rolesField.setItems(UserRoleName.values());
        rolesField.setItemLabelGenerator(UserRoleName::getDescription);
        rolesField.setLabel("Роли");
        rolesField.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

        updatedAtField.setReadOnly(true);
        createdAtField.setReadOnly(true);
    }

    public void withBean(User user) {
        this.binder.setBean(user);
        this.binder.bindInstanceFields(this);
    }
}
