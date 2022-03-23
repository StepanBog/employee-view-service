package tech.inno.odp.ui.views.employer;

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
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.data.containers.Requisites;
import tech.inno.odp.backend.data.containers.Tariff;
import tech.inno.odp.backend.data.containers.document.DocumentTemplateGroup;
import tech.inno.odp.backend.service.IDocumentTemplateService;
import tech.inno.odp.backend.service.IEmployeeService;
import tech.inno.odp.backend.service.IEmployerService;
import tech.inno.odp.ui.MainLayout;
import tech.inno.odp.ui.components.FlexBoxLayout;
import tech.inno.odp.ui.components.navigation.bar.AppBar;
import tech.inno.odp.ui.layout.size.Horizontal;
import tech.inno.odp.ui.layout.size.Vertical;
import tech.inno.odp.ui.util.UIUtils;
import tech.inno.odp.ui.views.ViewFrame;
import tech.inno.odp.ui.views.employee.EmployeeGrid;
import tech.inno.odp.ui.views.employer.form.DocumentTemplateGroupGrid;
import tech.inno.odp.ui.views.employer.form.EmployerSettingsForm;
import tech.inno.odp.ui.views.employer.form.EmployerTariffSettingsForm;
import tech.inno.odp.ui.views.requisites.RequisitesForm;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@PageTitle("Работодатель")
@Route(value = EmployerView.ROUTE, layout = MainLayout.class)
@RequiredArgsConstructor
public class EmployerView extends ViewFrame implements HasUrlParameter<String> {

    public static final String ROUTE = "employer-details";

    private final IEmployerService employerService;
    private final IDocumentTemplateService documentTemplateService;
    private final IEmployeeService employeeService;

    private Map<String, VerticalLayout> tabLayoutMap;
    private Employer employer;

    private final EmployerSettingsForm commonSettingsForm = new EmployerSettingsForm();
    private final EmployerTariffSettingsForm tariffSettingsForm = new EmployerTariffSettingsForm();
    private final RequisitesForm requisitesForm = new RequisitesForm();
    private final DocumentTemplateGroupGrid documentTemplateSettingsForm = new DocumentTemplateGroupGrid();
    private final EmployeeGrid employeeGrid = new EmployeeGrid();

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
        commonSettingsForm.init();
        commonSettingsForm.setVisible(true);
        commonSettingsForm.setId("commonSettingsForm");
        commonSettingsForm.setMaxWidth("800px");

        tariffSettingsForm.init();
        tariffSettingsForm.setVisible(false);
        tariffSettingsForm.setId("tariffSettingsForm");
        tariffSettingsForm.setMaxWidth("800px");

        requisitesForm.init();
        requisitesForm.setVisible(false);
        requisitesForm.setId("requisitesForm");
        requisitesForm.setMaxWidth("800px");

        documentTemplateSettingsForm.setDocumentTemplateService(documentTemplateService);
        documentTemplateSettingsForm.init();
        documentTemplateSettingsForm.setVisible(false);
        documentTemplateSettingsForm.setId("documentTemplateSettingsForm");

        employeeGrid.setFromEmployer(employer);
        employeeGrid.setEmployeeService(employeeService);
        employeeGrid.init();
        employeeGrid.setVisible(false);
        employeeGrid.setId("employeeGrid");

        this.tabLayoutMap =
                Map.of("commonSettingsForm", commonSettingsForm,
                        "tariffSettingsForm", tariffSettingsForm,
                        "requisitesForm", requisitesForm,
                        "documentTemplateSettingsForm", documentTemplateSettingsForm,
                        "employeeGrid", employeeGrid
                );

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.add(commonSettingsForm,
                requisitesForm,
                tariffSettingsForm,
                employeeGrid,
                documentTemplateSettingsForm
        );
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
        UI.getCurrent().getPage().setTitle(employer.getName());
    }

    private AppBar initAppBar() {
        AppBar appBar = MainLayout.get().getAppBar();

        appBar.addTab(createTab("commonSettingsForm", VaadinIcon.FORM.create(), "Настройки"));
        appBar.addTab(createTab("requisitesForm", VaadinIcon.MODAL_LIST.create(), "Реквизиты"));
        appBar.addTab(createTab("employeeGrid", VaadinIcon.USERS.create(), "Работники"));
        appBar.addTab(createTab("tariffSettingsForm", VaadinIcon.LIST.create(), "Тариф"));
        appBar.addTab(createTab("documentTemplateSettingsForm", VaadinIcon.BOOK.create(), "Шаблоны"));
        appBar.centerTabs();

        appBar.addTabSelectionListener(event -> {
            tabLayoutMap.get(event.getPreviousTab().getId().orElse("")).setVisible(false);
            tabLayoutMap.get(event.getSelectedTab().getId().orElse("")).setVisible(true);
        });

        appBar.setNaviMode(AppBar.NaviMode.CONTEXTUAL);
        appBar.getContextIcon().addClickListener(e -> UI.getCurrent().navigate(EmployerList.class));
        appBar.setTitle(employer.getName());
        return appBar;
    }

    @Override
    public void setParameter(BeforeEvent paramEvent, String param) {
        if (param.equalsIgnoreCase("new")) {
            employer = Employer.builder()
                    .tariff(Tariff.builder().build())
                    .requisites(Requisites.builder().build())
                    .build();
        } else {
            employer = employerService.findById(UUID.fromString(param));
        }
        commonSettingsForm.setEmployer(employer);
        tariffSettingsForm.setTariff(employer.getTariff());
        requisitesForm.setRequisites(employer.getRequisites());
        documentTemplateSettingsForm.setEmployer(employer);


        final Button save = UIUtils.createPrimaryButton("Сохранить");
        save.addClickListener(event -> {
            boolean isNew = StringUtils.isEmpty(employer.getId());

            Employer employer = commonSettingsForm.getBinder().getBean();
            employer.setRequisites(requisitesForm.getBinder().getBean());
            employer.setTariff(tariffSettingsForm.getBinder().getBean());
            employer = employerService.save(employer);

            if (isNew) {
                List<DocumentTemplateGroup> groupList = documentTemplateSettingsForm.getGroupList();
                for (DocumentTemplateGroup group : groupList) {
                    group.setEmployerId(employer.getId());
                    documentTemplateService.save(group);
                }
            }

            UI.getCurrent().navigate(EmployerList.class);
        });
        final Button cancel = UIUtils.createTertiaryButton("Отменить");
        cancel.addClickListener(event -> UI.getCurrent().navigate(EmployerList.class));

        HorizontalLayout buttonLayout = new HorizontalLayout(save, cancel);
        buttonLayout.setSpacing(true);
        buttonLayout.setPadding(true);

        setViewContent(createContent());
        setViewFooter(buttonLayout);
    }
}
