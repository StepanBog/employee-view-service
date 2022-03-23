package tech.inno.odp.ui.views.employee;

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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import tech.inno.odp.backend.data.containers.Employee;
import tech.inno.odp.backend.service.IDocumentService;
import tech.inno.odp.backend.service.IEmployeeService;
import tech.inno.odp.backend.service.IServiceStopIntervalService;
import tech.inno.odp.ui.MainLayout;
import tech.inno.odp.ui.components.FlexBoxLayout;
import tech.inno.odp.ui.components.navigation.bar.AppBar;
import tech.inno.odp.ui.layout.size.Horizontal;
import tech.inno.odp.ui.layout.size.Vertical;
import tech.inno.odp.ui.util.UIUtils;
import tech.inno.odp.ui.views.ViewFrame;
import tech.inno.odp.ui.views.employee.form.EmployeeDocumentsGrid;
import tech.inno.odp.ui.views.employee.form.EmployeeServiceStopIntervalForm;
import tech.inno.odp.ui.views.employee.form.EmployeeSettingsForm;
import tech.inno.odp.ui.views.employer.EmployerView;
import tech.inno.odp.ui.views.requisites.RequisitesForm;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@PageTitle("Работник")
@Route(value = EmployeeView.ROUTE, layout = MainLayout.class)
@RequiredArgsConstructor
public class EmployeeView extends ViewFrame implements BeforeEnterObserver {

    public static final String ROUTE = "employee-details";

    private final IDocumentService documentService;
    private final IEmployeeService employeeService;
    private final IServiceStopIntervalService serviceStopIntervalService;

    private Map<String, VerticalLayout> tabLayoutMap;
    private Employee employee;
    private boolean backToEmployerForm = false;

    private final EmployeeSettingsForm commonSettingsForm = new EmployeeSettingsForm();
    private final RequisitesForm requisitesForm = new RequisitesForm();
    private final EmployeeDocumentsGrid employeeDocumentsGrid = new EmployeeDocumentsGrid();
    private final EmployeeServiceStopIntervalForm employeeServiceStopIntervalForm = new EmployeeServiceStopIntervalForm();

    private Component createContent() {
        FlexBoxLayout content = new FlexBoxLayout(
                createEmployerUI()
        );
        content.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
        content.setMargin(Horizontal.AUTO, Vertical.RESPONSIVE_L);
        content.setWidth("100%");
        content.setHeightFull();
        return content;
    }

    private Component createEmployerUI() {
        commonSettingsForm.setEmployee(employee);
        commonSettingsForm.init();
        commonSettingsForm.setVisible(true);
        commonSettingsForm.setId("commonSettingsForm");
        commonSettingsForm.setMaxWidth("800px");

        requisitesForm.setRequisites(employee.getRequisites());
        requisitesForm.init();
        requisitesForm.setVisible(false);
        requisitesForm.setId("requisitesForm");
        requisitesForm.setMaxWidth("800px");

        employeeDocumentsGrid.setEmployee(employee);
        employeeDocumentsGrid.setDocumentService(documentService);
        employeeDocumentsGrid.init();
        employeeDocumentsGrid.setVisible(false);
        employeeDocumentsGrid.setId("employeeDocumentsGrid");

        employeeServiceStopIntervalForm.setEmployee(employee);
        employeeServiceStopIntervalForm.init();
        employeeServiceStopIntervalForm.setVisible(false);
        employeeServiceStopIntervalForm.setId("employeeServiceStopIntervalForm");

        tabLayoutMap =
                Map.of("commonSettingsForm", commonSettingsForm,
                        "requisitesForm", requisitesForm,
                        "employeeDocumentsGrid", employeeDocumentsGrid,
                        "employeeServiceStopIntervalForm", employeeServiceStopIntervalForm
                );

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.add(
                commonSettingsForm,
                requisitesForm,
                employeeServiceStopIntervalForm,
                employeeDocumentsGrid);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        return verticalLayout;
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
        UI.getCurrent().getPage().setTitle(getTitle());
    }

    private AppBar initAppBar() {
        AppBar appBar = MainLayout.get().getAppBar();

        appBar.addTab(createTab("commonSettingsForm", VaadinIcon.FORM.create(), "Настройки"));
        appBar.addTab(createTab("requisitesForm", VaadinIcon.MODAL_LIST.create(), "Реквизиты"));
        appBar.addTab(createTab("employeeServiceStopIntervalForm", VaadinIcon.MODAL_LIST.create(), "Стоп интервалы"));
        appBar.addTab(createTab("employeeDocumentsGrid", VaadinIcon.MODAL_LIST.create(), "Документы"));
        appBar.centerTabs();

        appBar.addTabSelectionListener(event -> {
            tabLayoutMap.get(event.getPreviousTab().getId().orElse("")).setVisible(false);
            tabLayoutMap.get(event.getSelectedTab().getId().orElse("")).setVisible(true);
        });

        appBar.setNaviMode(AppBar.NaviMode.CONTEXTUAL);
        appBar.getContextIcon().addClickListener(e -> navigateToBack());
        appBar.setTitle(getTitle());
        return appBar;
    }

    private String getTitle() {
        String title = "";
        if (employee.getRequisites() != null) {
            title = employee.getRequisites().getLastName() + " "
                    + employee.getRequisites().getFirstName() + " "
                    + employee.getRequisites().getPatronymicName();
        }
        return title;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        Map<String, List<String>> params = beforeEnterEvent.getLocation().getQueryParameters().getParameters();

        UUID employerId = UUID.fromString(params.get("employeeId").get(0));
        backToEmployerForm = Boolean.parseBoolean(
                params.getOrDefault("backToEmployerForm", List.of(Boolean.FALSE.toString())).get(0)
        );

        employee = employeeService.findById(employerId);
        employee.setServiceStopIntervals(
                serviceStopIntervalService.findByEmployeeId(
                        UUID.fromString(employee.getId())
                )
        );

        final Button toEmployer = UIUtils.createPrimaryButton("К работодателю");
        toEmployer.addClickListener(event -> {
            UI.getCurrent().navigate(EmployerView.class, employee.getEmployerId());
        });

        final Button save = UIUtils.createPrimaryButton("Сохранить");
        save.addClickListener(event -> {

            Employee employee = commonSettingsForm.getBinder().getBean();
            employee.setRequisites(requisitesForm.getBinder().getBean());
            employee = employeeService.save(employee);
            navigateToBack();
        });
        final Button cancel = UIUtils.createTertiaryButton("Отменить");
        cancel.addClickListener(event -> navigateToBack());

        HorizontalLayout buttonFooterLayout = new HorizontalLayout(save, toEmployer, cancel);
        buttonFooterLayout.setSpacing(true);
        buttonFooterLayout.setPadding(true);

        setViewContent(createContent());
        setViewFooter(buttonFooterLayout);
    }

    private void navigateToBack() {
        if (backToEmployerForm) {
            UI.getCurrent().navigate(EmployerView.class, employee.getEmployerId());
        } else {
            UI.getCurrent().navigate(EmployeeList.class);
        }
    }
}
