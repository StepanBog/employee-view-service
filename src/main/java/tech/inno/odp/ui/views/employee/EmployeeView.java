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
import tech.inno.odp.backend.data.containers.Transaction;
import tech.inno.odp.backend.service.*;
import tech.inno.odp.ui.MainLayout;
import tech.inno.odp.ui.components.FlexBoxLayout;
import tech.inno.odp.ui.components.navigation.bar.AppBar;
import tech.inno.odp.ui.layout.size.Horizontal;
import tech.inno.odp.ui.layout.size.Vertical;
import tech.inno.odp.ui.util.UIUtils;
import tech.inno.odp.ui.views.ViewFrame;
import tech.inno.odp.ui.views.employee.form.EmployeeDocumentsGrid;
import tech.inno.odp.ui.views.employee.form.EmployeeSalaryForm;
import tech.inno.odp.ui.views.employee.form.EmployeeServiceStopIntervalForm;
import tech.inno.odp.ui.views.employee.form.EmployeeSettingsForm;
import tech.inno.odp.ui.views.employer.EmployerView;
import tech.inno.odp.ui.views.requisites.RequisitesForm;
import tech.inno.odp.ui.views.transaction.TransactionGridWithFilter;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@PageTitle("Работник")
@Route(value = EmployeeView.ROUTE, layout = MainLayout.class)
@RequiredArgsConstructor
public class EmployeeView extends ViewFrame implements BeforeEnterObserver {

    public static final String ROUTE = "employee-details";

    private final IEmployeeService employeeService;
    private final IDocumentService documentService;
    private final ISalaryService salaryService;
    private final ITransactionService transactionService;
    private final IServiceStopIntervalService serviceStopIntervalService;

    private Map<String, VerticalLayout> tabLayoutMap;
    private Employee employee;
    private boolean backToEmployerForm = false;

    private final EmployeeSettingsForm employeeSettingsForm = new EmployeeSettingsForm();
    private final RequisitesForm requisitesForm = new RequisitesForm();
    private final EmployeeDocumentsGrid employeeDocumentsGrid = new EmployeeDocumentsGrid();
    private final TransactionGridWithFilter transactionGridWithFilter = new TransactionGridWithFilter();
    private final EmployeeSalaryForm employeeSalaryForm = new EmployeeSalaryForm();
    private final EmployeeServiceStopIntervalForm employeeServiceStopIntervalForm = new EmployeeServiceStopIntervalForm();

    @PostConstruct
    public void init() {
        final Button toEmployer = UIUtils.createPrimaryButton("К работодателю");
        toEmployer.addClickListener(event -> {
            UI.getCurrent().navigate(EmployerView.class, employee.getEmployerId());
        });

        final Button save = UIUtils.createPrimaryButton("Сохранить");
        save.addClickListener(event -> {

            Employee employee = employeeSettingsForm.getBinder().getBean();
            employee.setRequisites(requisitesForm.getBinder().getBean());
            employeeService.save(employee);
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

    private Component createContent() {
        FlexBoxLayout content = new FlexBoxLayout(
                createEmployerUI()
        );
        content.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
        content.setMargin(Horizontal.AUTO, Vertical.RESPONSIVE_X);
        content.setWidth("100%");
        content.setHeightFull();
        return content;
    }

    private Component createEmployerUI() {
        employeeSettingsForm.init();
        employeeSettingsForm.setVisible(true);
        employeeSettingsForm.setMaxWidth("800px");

        requisitesForm.init();
        requisitesForm.setVisible(false);
        requisitesForm.setMaxWidth("800px");

        employeeDocumentsGrid.setDocumentService(documentService);
        employeeDocumentsGrid.init();
        employeeDocumentsGrid.setVisible(false);

        transactionGridWithFilter.setTransactionService(transactionService);
        transactionGridWithFilter.init();
        transactionGridWithFilter.setVisible(false);

        employeeSalaryForm.setSalaryService(salaryService);
        employeeSalaryForm.init();
        employeeSalaryForm.setVisible(false);

        employeeServiceStopIntervalForm.setServiceStopIntervalService(serviceStopIntervalService);
        employeeServiceStopIntervalForm.init();
        employeeServiceStopIntervalForm.setVisible(false);

        tabLayoutMap =
                Map.of(EmployeeSettingsForm.ID, employeeSettingsForm,
                        RequisitesForm.ID, requisitesForm,
                        EmployeeDocumentsGrid.ID, employeeDocumentsGrid,
                        EmployeeSalaryForm.ID, employeeSalaryForm,
                        EmployeeServiceStopIntervalForm.ID, employeeServiceStopIntervalForm,
                        TransactionGridWithFilter.ID, transactionGridWithFilter
                );

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setPadding(false);
        verticalLayout.setMargin(false);
        verticalLayout.setSpacing(false);
        verticalLayout.add(
                employeeSettingsForm,
                requisitesForm,
                employeeSalaryForm,
                transactionGridWithFilter,
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

        appBar.addTab(createTab(EmployeeSettingsForm.ID, VaadinIcon.FORM.create(), "Настройки"));
        appBar.addTab(createTab(RequisitesForm.ID, VaadinIcon.MODAL_LIST.create(), "Реквизиты"));
        appBar.addTab(createTab(TransactionGridWithFilter.ID, VaadinIcon.MONEY.create(), "Платежи"));
        appBar.addTab(createTab(EmployeeSalaryForm.ID, VaadinIcon.BOOK.create(), "Зарплата"));
        appBar.addTab(createTab(EmployeeServiceStopIntervalForm.ID, VaadinIcon.MODAL_LIST.create(), "Стоп интервалы"));
        appBar.addTab(createTab(EmployeeDocumentsGrid.ID, VaadinIcon.MODAL_LIST.create(), "Документы"));
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

        requisitesForm.withBean(employee.getRequisites());
        employeeSettingsForm.withBean(employee);
        employeeServiceStopIntervalForm.withBean(employee);
        employeeDocumentsGrid.withBean(employee);

        employeeSalaryForm.withBean(employee);
        transactionGridWithFilter.withFilter(
                Transaction.builder()
                        .employeeId(employee.getId())
                        .employerId(employee.getEmployerId())
                        .build()
        );
    }

    private void navigateToBack() {
        if (backToEmployerForm) {
            UI.getCurrent().navigate(EmployerView.class, employee.getEmployerId());
        } else {
            UI.getCurrent().navigate(EmployeeList.class);
        }
    }
}
