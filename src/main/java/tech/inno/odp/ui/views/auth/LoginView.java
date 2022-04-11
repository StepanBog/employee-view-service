package tech.inno.odp.ui.views.auth;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tech.inno.odp.backend.config.security.CustomAuthenticationProvider;
import tech.inno.odp.ui.views.Home;

import javax.annotation.PostConstruct;

/**
 * @author VKozlov
 */
@Slf4j
@Route(value = LoginView.ROUTE)
@PageTitle("Login")
@RequiredArgsConstructor
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    public static final String ROUTE = "login";

    private final CustomAuthenticationProvider authenticationProvider;

    private LoginOverlay loginOverlay = new LoginOverlay();

    @PostConstruct
    public void init() {
        loginOverlay.setOpened(true);

        LoginI18n i18n = LoginI18n.createDefault();

        LoginI18n.Header i18nHeader = new LoginI18n.Header();
        i18nHeader.setTitle("ODP admin");
        i18nHeader.setDescription("Панель администратора сервиса ODP");
        i18n.setHeader(i18nHeader);

        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Данные для входа");
        i18nForm.setUsername("Логин");
        i18nForm.setPassword("Пароль");
        i18nForm.setSubmit("Вход");
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Неправильное имя пользователя или пароль");
        i18nErrorMessage.setMessage("Убедитесь, что вы ввели правильное имя пользователя и пароль, и повторите попытку");
        i18n.setErrorMessage(i18nErrorMessage);

        loginOverlay.setForgotPasswordButtonVisible(false);
        loginOverlay.setI18n(i18n);

        loginOverlay.addLoginListener(event -> {
            try {
                Authentication authenticated = authenticationProvider.login(
                        new UsernamePasswordAuthenticationToken(event.getUsername(), event.getPassword())
                );
                SecurityContextHolder.getContext().setAuthentication(authenticated);

                loginOverlay.close();
                UI.getCurrent().navigate(Home.class);
            } catch (Exception e) {
                log.error("Error login to admin UI using username {}", event.getUsername());
                loginOverlay.setError(true);
            }
        });

        add(loginOverlay);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // inform the user about an authentication error
        if (event.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginOverlay.setError(true);
        }
    }
}
