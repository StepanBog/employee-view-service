package ru.bogdanov.diplom.ui.views.employer;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import ru.bogdanov.diplom.backend.data.containers.Employer;
import ru.bogdanov.diplom.backend.data.containers.Requisites;
import ru.bogdanov.diplom.backend.service.IEmployeeService;
import ru.bogdanov.diplom.backend.service.IEmployerService;
import ru.bogdanov.diplom.ui.EmployeeMainLayout;
import ru.bogdanov.diplom.ui.components.FlexBoxLayout;
import ru.bogdanov.diplom.ui.components.navigation.bar.AppBar;
import ru.bogdanov.diplom.ui.handler.SaveButtonErrorHandler;
import ru.bogdanov.diplom.ui.layout.size.Horizontal;
import ru.bogdanov.diplom.ui.layout.size.Vertical;
import ru.bogdanov.diplom.ui.util.UIUtils;
import ru.bogdanov.diplom.ui.views.ViewFrame;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.UUID;

@PageTitle("Работодатель")
@Route(value = EmployerView.ROUTE, layout = EmployeeMainLayout.class)
@RequiredArgsConstructor
public class EmployerView extends ViewFrame implements HasUrlParameter<String> {

    public static final String ROUTE = "employer-details";

    private final IEmployerService employerService;
    private final IEmployeeService employeeService;

    private Map<String, VerticalLayout> tabLayoutMap;
    private Employer employer;
    private String param;
    private Button save, cancel, edit;

    @PostConstruct
    public void init() {
        save  = UIUtils.createPrimaryButton("Сохранить");
        cancel = UIUtils.createTertiaryButton("Отменить");
        edit = UIUtils.createPrimaryButton("Редактировать");
        setFormEditable(false);

        save.addClickListener(this::onSaveEvent);

        cancel.addClickListener(event -> {
            setFormEditable(false);

            getEmployer(param);
        });
        cancel.addClickShortcut(Key.ESCAPE);

        edit.addClickListener(event -> {
            setFormEditable(true);
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(save, cancel, edit);
        buttonLayout.setSpacing(true);
        buttonLayout.setPadding(true);

        setViewContent(createContent());
        setViewFooter(buttonLayout);
    }

    private void onSaveEvent(ClickEvent<Button> event) {
        boolean isNew = StringUtils.isEmpty(employer.getId());
        VaadinSession.getCurrent().setErrorHandler(new SaveButtonErrorHandler());
    }

    private void setFormEditable(boolean flag) {
        edit.setVisible(!flag);
        save.setVisible(flag);
        cancel.setVisible(flag);
    }

    private Component createContent() {
        FlexBoxLayout content = new FlexBoxLayout();
        content.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
        content.setMargin(Horizontal.XS, Vertical.XS);
        content.setSizeFull();
        return content;
    }

    private Tab createTab(String id, Icon icon, String label) {
        Tab tab = new Tab(icon, new Span(label));
        tab.setId(id);
        return tab;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        initAppBar();
        UI.getCurrent().getPage().setTitle(employer.getName());
    }

    private void initAppBar() {
        AppBar appBar = EmployeeMainLayout.get().getAppBar();
        appBar.centerTabs();

        appBar.addTabSelectionListener(event -> {
            tabLayoutMap.get(event.getPreviousTab().getId().orElse("")).setVisible(false);
            tabLayoutMap.get(event.getSelectedTab().getId().orElse("")).setVisible(true);
        });

        appBar.setNaviMode(AppBar.NaviMode.CONTEXTUAL);
        appBar.getContextIcon().addClickListener(e -> UI.getCurrent().navigate(EmployerList.class));
        appBar.setTitle(employer.getName());
    }

    @Override
    public void setParameter(BeforeEvent paramEvent, String param) {
        this.param = param;
        getEmployer(param);
    }

    private void getEmployer(String param) {
        if (param.equalsIgnoreCase("new")) {
            employer = Employer.builder()
                    .requisites(Requisites.builder().build())
                    .build();
        } else {
            employer = employerService.findById(UUID.fromString(param));
        }
    }
}
