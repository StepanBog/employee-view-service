package tech.inno.odp.ui.views.user;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tech.inno.odp.backend.data.containers.User;
import tech.inno.odp.backend.service.IUserService;
import tech.inno.odp.ui.MainLayout;
import tech.inno.odp.ui.components.FlexBoxLayout;
import tech.inno.odp.ui.components.navigation.bar.AppBar;
import tech.inno.odp.ui.layout.size.Horizontal;
import tech.inno.odp.ui.layout.size.Vertical;
import tech.inno.odp.ui.util.UIUtils;
import tech.inno.odp.ui.views.ViewFrame;
import tech.inno.odp.ui.views.employee.EmployeeView;
import tech.inno.odp.ui.views.employer.EmployerView;
import tech.inno.odp.ui.views.user.form.TokenSettingsForm;
import tech.inno.odp.ui.views.user.form.UserSettingsForm;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.UUID;

@PageTitle("Пользователь")
@Route(value = UserView.ROUTE, layout = MainLayout.class)
@RequiredArgsConstructor
public class UserView extends ViewFrame implements HasUrlParameter<String> {

    public static final String ROUTE = "user-details";

    private final IUserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    private Map<String, VerticalLayout> tabLayoutMap;

    private final UserSettingsForm userSettingsForm = new UserSettingsForm();;
    private final TokenSettingsForm tokenSettingsForm = new TokenSettingsForm();

    private Button toEmployer;
    private Button toEmployee;

    private User user;

    private String fromDbPassword;

    @PostConstruct
    public void init() {
        toEmployer = UIUtils.createPrimaryButton("К работодателю");
        toEmployer.addClickListener(event -> {
            UI.getCurrent().navigate(EmployerView.class, user.getEmployerId());
        });

        toEmployee = UIUtils.createPrimaryButton("К работнику");
        toEmployee.addClickListener(event -> {
            Map<String, String> params = Map.of("employeeId", user.getEmployeeId());
            UI.getCurrent().navigate(EmployeeView.ROUTE, QueryParameters.simple(params));
        });

        final Button save = UIUtils.createPrimaryButton("Сохранить");
        save.addClickListener(event -> {
            User user = userSettingsForm.getBinder().getBean();
            if (!fromDbPassword.equals(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userService.save(user);
            UI.getCurrent().navigate(UserList.class);
        });

        final Button cancel = UIUtils.createTertiaryButton("Отменить");
        cancel.addClickListener(event -> UI.getCurrent().navigate(UserList.class));

        HorizontalLayout buttonLayout = new HorizontalLayout(save, toEmployer, toEmployee, cancel);

        buttonLayout.setSpacing(true);
        buttonLayout.setPadding(true);

        setViewContent(createContent());
        setViewFooter(buttonLayout);
    }

    private Component createContent() {
        FlexBoxLayout content = new FlexBoxLayout(
                createUserUI()
        );
        content.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
        content.setMargin(Horizontal.XS, Vertical.XS);
        content.setSizeFull();
        return content;
    }

    private Component createUserUI() {
        userSettingsForm.init();
        userSettingsForm.setVisible(true);
        userSettingsForm.setMaxWidth("800px");

        tokenSettingsForm.init();
        tokenSettingsForm.setVisible(false);
        tokenSettingsForm.setMaxWidth("800px");

        this.tabLayoutMap = Map.of(UserSettingsForm.ID, userSettingsForm, TokenSettingsForm.ID, tokenSettingsForm);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setPadding(false);
        verticalLayout.setMargin(false);
        verticalLayout.setSpacing(false);
        verticalLayout.setSizeFull();
        verticalLayout.add(userSettingsForm, tokenSettingsForm);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        return verticalLayout;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        initAppBar();
        UI.getCurrent().getPage().setTitle(user.getUsername());
    }

    private void initAppBar() {
        AppBar appBar = MainLayout.get().getAppBar();

        appBar.addTab(createTab(UserSettingsForm.ID, VaadinIcon.FORM.create(), "Настройки"));
        appBar.addTab(createTab(TokenSettingsForm.ID, VaadinIcon.MODAL_LIST.create(), "Время жизни токенов"));
        appBar.centerTabs();

        appBar.addTabSelectionListener(event -> {
            tabLayoutMap.get(event.getPreviousTab().getId().orElse("")).setVisible(false);
            tabLayoutMap.get(event.getSelectedTab().getId().orElse("")).setVisible(true);
        });

        appBar.setNaviMode(AppBar.NaviMode.CONTEXTUAL);
        appBar.getContextIcon().addClickListener(e -> UI.getCurrent().navigate(UserList.class));
        appBar.setTitle(user.getUsername());
    }

    private Tab createTab(String id, Icon icon, String label) {
        Tab tab = new Tab(icon, new Span(label));
        tab.setId(id);
        return tab;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String param) {
        user = userService.findById(UUID.fromString(param));
        fromDbPassword = user.getPassword();
        userSettingsForm.withBean(user);
        tokenSettingsForm.withBean(user.getSettings());

        if (StringUtils.isEmpty(user.getEmployerId())) {
            toEmployer.setEnabled(false);
        }
        if (StringUtils.isEmpty(user.getEmployeeId())) {
            toEmployee.setEnabled(false);
        }
    }
}
