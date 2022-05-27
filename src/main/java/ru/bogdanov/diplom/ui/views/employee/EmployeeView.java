package ru.bogdanov.diplom.ui.views.employee;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.ErrorHandler;
import com.vaadin.flow.server.VaadinSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.bogdanov.diplom.backend.data.containers.Employee;
import ru.bogdanov.diplom.backend.data.containers.Transaction;
import ru.bogdanov.diplom.backend.data.containers.User;
import ru.bogdanov.diplom.backend.service.IEmployeeService;
import ru.bogdanov.diplom.backend.service.IEmployerService;
import ru.bogdanov.diplom.backend.service.ISalaryService;
import ru.bogdanov.diplom.backend.service.ITransactionService;
import ru.bogdanov.diplom.ui.EmployeeMainLayout;
import ru.bogdanov.diplom.ui.components.FlexBoxLayout;
import ru.bogdanov.diplom.ui.components.navigation.bar.AppBar;
import ru.bogdanov.diplom.ui.layout.size.Horizontal;
import ru.bogdanov.diplom.ui.layout.size.Vertical;
import ru.bogdanov.diplom.ui.views.ViewFrame;
import ru.bogdanov.diplom.ui.views.employee.form.EmployeeSalaryForm;
import ru.bogdanov.diplom.ui.views.employer.EmployerView;
import ru.bogdanov.diplom.ui.views.employer.form.EmployerForm;
import ru.bogdanov.diplom.ui.views.requisites.RequisitesForm;
import ru.bogdanov.diplom.ui.views.transaction.TransactionGridWithFilter;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.UUID;

@Slf4j
@PageTitle("Работник")
@Route(value = EmployeeView.ROUTE, layout = EmployeeMainLayout.class)
@RequiredArgsConstructor
public class EmployeeView extends ViewFrame implements BeforeEnterObserver {

    public static final String ROUTE = "employee-details";

    private final IEmployeeService employeeService;
    private final ISalaryService salaryService;
    private final ITransactionService transactionService;
    private final IEmployerService employerService;

    private Map<String, VerticalLayout> tabLayoutMap;
    private Employee employee;

    private final RequisitesForm requisitesForm = new RequisitesForm();
    private final TransactionGridWithFilter transactionGridWithFilter = new TransactionGridWithFilter();
    private final EmployeeSalaryForm employeeSalaryForm = new EmployeeSalaryForm();
    private final EmployerForm employerForm = new EmployerForm();
    @PostConstruct
    public void init() {
        VaadinSession.getCurrent().setErrorHandler(  (ErrorHandler) error -> {log.error("Uncaught UI exception",
                error.getThrowable());
        Notification.show(
                "Превышение допустимой суммы выплаты");});
        setViewContent(createContent());
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


        transactionGridWithFilter.setTransactionService(transactionService);
        transactionGridWithFilter.init();
        transactionGridWithFilter.setVisible(false);

        employeeSalaryForm.setSalaryService(salaryService);
        employeeSalaryForm.setTransactionService(transactionService);
        employeeSalaryForm.init();
        employeeSalaryForm.setVisible(false);

        requisitesForm.init();
        requisitesForm.setVisible(false);
        requisitesForm.setMaxWidth("800px");

        employerForm.init();
        employerForm.setVisible(false);
        tabLayoutMap =
                Map.of(
                        RequisitesForm.ID, requisitesForm,
                        EmployeeSalaryForm.ID, employeeSalaryForm,
                        TransactionGridWithFilter.ID, transactionGridWithFilter,
                        EmployerForm.ID, employerForm
                );

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setPadding(false);
        verticalLayout.setMargin(false);
        verticalLayout.setSpacing(false);
        verticalLayout.add(
                requisitesForm,
                employeeSalaryForm,
                transactionGridWithFilter,
                employerForm);
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
        AppBar appBar = EmployeeMainLayout.get().getAppBar();

        appBar.addTab(createTab(RequisitesForm.ID, VaadinIcon.MODAL_LIST.create(), "Реквизиты"));
        appBar.addTab(createTab(TransactionGridWithFilter.ID, VaadinIcon.MONEY.create(), "Платежи"));
        appBar.addTab(createTab(EmployeeSalaryForm.ID, VaadinIcon.BOOK.create(), "Зарплата"));
        appBar.addTab(createTab(EmployerForm.ID, VaadinIcon.BOOK.create(), "Работодатель"));
        appBar.centerTabs();

        appBar.addTabSelectionListener(event -> {
            tabLayoutMap.get(event.getPreviousTab().getId().orElse("")).setVisible(false);
            tabLayoutMap.get(event.getSelectedTab().getId().orElse("")).setVisible(true);
        });
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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID employeeId = UUID.fromString(user.getEmployeeId());
        employee = employeeService.findById(employeeId);
        requisitesForm.withBean(employee.getRequisites(),employee);
        employerForm.withBean(employerService.findById(UUID.fromString(employee.getEmployerId())));
        employeeSalaryForm.withBean(employee);
        transactionGridWithFilter.withFilter(
                Transaction.builder()
                        .employeeId(employee.getId())
                        .build()
        );
    }

}
