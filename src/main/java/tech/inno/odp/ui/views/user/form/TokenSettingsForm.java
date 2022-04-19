package tech.inno.odp.ui.views.user.form;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.converter.StringToLongConverter;
import lombok.Getter;
import tech.inno.odp.backend.data.containers.UserSettings;
import tech.inno.odp.ui.util.LumoStyles;

public class TokenSettingsForm extends VerticalLayout {

    public static final String ID = "tokenSettingsForm";

    @Getter
    private BeanValidationBinder<UserSettings> binder;

    @PropertyId("tokenTtl")
    private final TextField tokenTtlField = new TextField("Время жизни токена в секундах");
    @PropertyId("refreshTokenTtl")
    private final TextField  refreshTokenTtlField = new TextField("Время жизни токена обновления в секундах");

    public void init() {
        setId(ID);
        this.binder = new BeanValidationBinder<>(UserSettings.class);
        this.binder.forField(tokenTtlField)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter("Неверное значение токена"))
                .bind(UserSettings::getTokenTtl, UserSettings::setTokenTtl);
        this.binder.forField(refreshTokenTtlField)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter("Неверное значение токена"))
                .bind(UserSettings::getRefreshTokenTtl, UserSettings::setRefreshTokenTtl);
        add(createForm());
    }

    private FormLayout createForm() {
        FormLayout formLayout = new FormLayout();

        formLayout.add(tokenTtlField);
        formLayout.add(refreshTokenTtlField);

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

    public void withBean(UserSettings userSettings) {
        this.binder.removeBean();
        this.binder.setBean(userSettings);
        this.binder.bindInstanceFields(this);
    }
}
