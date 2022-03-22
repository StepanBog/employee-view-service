package tech.inno.odp.ui.views.employee;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.URLEncodedUtils;
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
import tech.inno.odp.ui.views.employer.EmployerList;
import tech.inno.odp.ui.views.employer.EmployerView;
import tech.inno.odp.ui.views.requisites.RequisitesForm;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@PageTitle("Работник")
@Route(value = "employee-details", layout = MainLayout.class)
@RequiredArgsConstructor
public class EmployeeView extends ViewFrame implements HasUrlParameter<String> {

    private final IDocumentService documentService;
    private final IEmployeeService employeeService;
    private final IServiceStopIntervalService serviceStopIntervalService;

    private Map<String, VerticalLayout> tabLayoutMap;
    private Employee employee;

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

        requisitesForm.setRequisites(employee.getRequisites());
        requisitesForm.init();
        requisitesForm.setVisible(false);
        requisitesForm.setId("requisitesForm");

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
        appBar.getContextIcon().addClickListener(e -> UI.getCurrent().navigate(EmployerList.class));
        appBar.setTitle(getTitle());
        return appBar;
    }

    @Override
    public void setParameter(BeforeEvent paramEvent, String param) {
        URLEncodedUtils.parse(param, StandardCharsets.UTF_8);

        employee = employeeService.findById(UUID.fromString(param));
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

            UI.getCurrent().navigate(EmployerList.class);
        });
        final Button cancel = UIUtils.createTertiaryButton("Отменить");
        cancel.addClickListener(event -> {
            UI.getCurrent().navigate(EmployerList.class);
        });


        HorizontalLayout buttonFooterLayout = new HorizontalLayout(save, toEmployer, cancel);
        buttonFooterLayout.setSpacing(true);
        buttonFooterLayout.setPadding(true);

        setViewContent(createContent());
        setViewFooter(buttonFooterLayout);
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
}
