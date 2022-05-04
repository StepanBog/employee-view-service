package tech.inno.odp.ui.views.employer;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
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
import com.vaadin.flow.server.VaadinSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.data.containers.Requisites;
import tech.inno.odp.backend.data.containers.document.DocumentTemplateGroup;
import tech.inno.odp.backend.service.IDocumentTemplateService;
import tech.inno.odp.backend.service.IEmployeeService;
import tech.inno.odp.backend.service.IEmployerService;
import tech.inno.odp.ui.MainLayout;
import tech.inno.odp.ui.components.FlexBoxLayout;
import tech.inno.odp.ui.components.navigation.bar.AppBar;
import tech.inno.odp.ui.handler.SaveButtonErrorHandler;
import tech.inno.odp.ui.layout.size.Horizontal;
import tech.inno.odp.ui.layout.size.Vertical;
import tech.inno.odp.ui.util.UIUtils;
import tech.inno.odp.ui.views.ViewFrame;
import tech.inno.odp.ui.views.employer.form.DocumentGroupGrid;
import tech.inno.odp.ui.views.employer.form.EmployerSettingsForm;
import tech.inno.odp.ui.views.employer.form.EmployerTariffSettingsForm;
import tech.inno.odp.utils.NotificationUtils;

import javax.annotation.PostConstruct;
import java.util.HashSet;
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
    private String param;
    private Button save, cancel, edit;

    private final EmployerSettingsForm commonSettingsForm = new EmployerSettingsForm();
    private final EmployerTariffSettingsForm tariffSettingsForm = new EmployerTariffSettingsForm();
    private final DocumentGroupGrid documentGroupGrid = new DocumentGroupGrid();

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
            commonSettingsForm.withBean(employer);
            tariffSettingsForm.withBean(employer);
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

        if (commonSettingsForm.getBinder().validate().isOk()) {
            Employer employer = commonSettingsForm.getBinder().getBean();
            employer = employerService.save(employer);

            if (isNew) {
                List<DocumentTemplateGroup> groupList = documentGroupGrid.getGroupList();
                for (DocumentTemplateGroup group : groupList) {
                    group.setEmployerId(employer.getId());
                    documentTemplateService.save(group);
                }
            }
            UI.getCurrent().navigate(EmployerList.class);
            NotificationUtils.showNotificationOnSave();

            setFormEditable(false);
        }
    }

    private void setFormEditable(boolean flag) {
        edit.setVisible(!flag);
        save.setVisible(flag);
        cancel.setVisible(flag);
        tariffSettingsForm.setFieldsReadOnly(!flag);
        commonSettingsForm.setFieldsReadOnly(!flag);
    }

    private Component createContent() {
        FlexBoxLayout content = new FlexBoxLayout(createEmployerUI());
        content.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
        content.setMargin(Horizontal.XS, Vertical.XS);
        content.setSizeFull();
        return content;
    }

    private Component createEmployerUI() {
        commonSettingsForm.setEmployeeService(employeeService);
        commonSettingsForm.init();
        commonSettingsForm.setVisible(true);

        tariffSettingsForm.init();
        tariffSettingsForm.setVisible(false);

        documentGroupGrid.setDocumentTemplateService(documentTemplateService);
        documentGroupGrid.init();
        documentGroupGrid.setVisible(false);

        this.tabLayoutMap =
                Map.of( EmployerSettingsForm.ID, commonSettingsForm,
                        EmployerTariffSettingsForm.ID, tariffSettingsForm,
                        DocumentGroupGrid.ID, documentGroupGrid);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setPadding(false);
        verticalLayout.setMargin(false);
        verticalLayout.setSpacing(false);
        verticalLayout.setSizeFull();
        verticalLayout.add(
                commonSettingsForm,
                tariffSettingsForm,
                documentGroupGrid);
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

    private void initAppBar() {
        AppBar appBar = MainLayout.get().getAppBar();

        appBar.addTab(createTab(EmployerSettingsForm.ID, VaadinIcon.FORM.create(), "Настройки"));
        appBar.addTab(createTab(EmployerTariffSettingsForm.ID, VaadinIcon.LIST.create(), "Тариф"));
        appBar.addTab(createTab(DocumentGroupGrid.ID, VaadinIcon.BOOK.create(), "Шаблоны"));
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

        commonSettingsForm.withBean(employer);
        tariffSettingsForm.withBean(employer);
        documentGroupGrid.withBean(employer);
    }

    private void getEmployer(String param) {
        if (param.equalsIgnoreCase("new")) {
            employer = Employer.builder()
                    .tariffs(new HashSet<>())
                    .contacts(new HashSet<>())
                    .requisites(Requisites.builder().build())
                    .build();
        } else {
            employer = employerService.findById(UUID.fromString(param));
        }
    }
}
