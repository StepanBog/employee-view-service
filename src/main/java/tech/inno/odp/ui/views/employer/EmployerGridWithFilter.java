package tech.inno.odp.ui.views.employer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.data.enums.EmployerStatus;
import tech.inno.odp.backend.service.IEmployerService;
import tech.inno.odp.ui.components.Badge;
import tech.inno.odp.ui.components.ColumnToggleContextMenu;
import tech.inno.odp.ui.components.field.CustomTextField;
import tech.inno.odp.ui.components.grid.PaginatedGrid;
import tech.inno.odp.ui.util.IconSize;
import tech.inno.odp.ui.util.UIUtils;
import tech.inno.odp.ui.util.converter.LocalDateToLocalDateTimeConverter;
import tech.inno.odp.ui.util.converter.StringToStringWithNullValueConverter;

import java.time.format.DateTimeFormatter;

public class EmployerGridWithFilter extends EmployerGrid {

    public static final String ID = "employerGridWithFilter";

    @PropertyId("id")
    private CustomTextField idField = new CustomTextField();
    @PropertyId("name")
    private CustomTextField nameField = new CustomTextField();

    @PropertyId("email")
    private EmailField emailField = new EmailField();
    @PropertyId("status")
    private ComboBox<EmployerStatus> statusField = new ComboBox();
    @PropertyId("updatedAt")
    private DatePicker updatedAtField = new DatePicker();
    @PropertyId("createdAt")
    private DatePicker createdAtField = new DatePicker();

    @Getter
    private BeanValidationBinder<Employer> binder;

    public void init() {
        setId(ID);
        setSizeFull();
        initFields();

        initDataProvider();

        this.binder = new BeanValidationBinder<>(Employer.class);
        this.binder.setBean(this.employerFilter);

        LocalDateToLocalDateTimeConverter localDateTimeConverter = new LocalDateToLocalDateTimeConverter();
        this.binder.forField(updatedAtField)
                .withConverter(localDateTimeConverter)
                .bind(Employer::getUpdatedAt, Employer::setUpdatedAt);
        this.binder.forField(createdAtField)
                .withConverter(localDateTimeConverter)
                .bind(Employer::getCreatedAt, Employer::setCreatedAt);
        this.binder.setValidatorsDisabled(true);
        this.binder.bindInstanceFields(this);

        add(createContent());
    }

    private void initFields() {

        StringToStringWithNullValueConverter stringToStringWithNullValueConverter = new StringToStringWithNullValueConverter();

        idField.setConverters(stringToStringWithNullValueConverter);
        idField.setPlaceholder("ID");
        idField.setValueChangeMode(ValueChangeMode.EAGER);
        idField.setClearButtonVisible(true);
        idField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        idField.setWidthFull();
        idField.getStyle().set("max-width", "100%");
        idField.addValueChangeListener(e -> {
            employerFilter.setId(StringUtils.isEmpty(e.getValue()) ? null : e.getValue());
            grid.getDataProvider().refreshAll();
            grid.refreshPaginator();
        });

        nameField.setConverters(stringToStringWithNullValueConverter);
        nameField.setPlaceholder("Работодатель");
        nameField.setValueChangeMode(ValueChangeMode.EAGER);
        nameField.setClearButtonVisible(true);
        nameField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        nameField.setWidthFull();
        nameField.getStyle().set("max-width", "100%");
        nameField.addValueChangeListener(e -> {
            employerFilter.setName(StringUtils.isEmpty(e.getValue()) ? null : e.getValue());
            grid.getDataProvider().refreshAll();
            grid.refreshPaginator();
        });

        statusField.setPlaceholder("Статус");
        statusField.setItems(EmployerStatus.values());
        statusField.setItemLabelGenerator(EmployerStatus::getDescription);
        statusField.setClearButtonVisible(true);
        statusField.setWidthFull();
        statusField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        statusField.getStyle().set("max-width", "100%");
        statusField.addValueChangeListener(
                s -> {
                    employerFilter.setStatus(s.getValue());
                    grid.getDataProvider().refreshAll();
                    grid.refreshPaginator();
                }
        );

        emailField.setPlaceholder("Email");
        emailField.setClearButtonVisible(true);
        emailField.setWidthFull();
        emailField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        emailField.getStyle().set("max-width", "100%");
        emailField.addValueChangeListener(
                s -> {
                    employerFilter.setEmail(s.getValue());
                    grid.getDataProvider().refreshAll();
                    grid.refreshPaginator();
                }
        );

        updatedAtField.setPlaceholder("Дата обновления");
        updatedAtField.setClearButtonVisible(true);
        updatedAtField.setWidthFull();
        updatedAtField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        updatedAtField.getStyle().set("max-width", "100%");
        updatedAtField.addValueChangeListener(
                e -> {
                    //TODO сделать фильтрацию
//                    employeeFilter.setUpdatedAt(e.getValue().atStartOfDay());
//                    grid.getDataProvider().refreshAll();
//                    grid.refreshPaginator();
                });

        createdAtField.setPlaceholder("Дата создания");
        createdAtField.setClearButtonVisible(true);
        createdAtField.setWidthFull();
        createdAtField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
        createdAtField.getStyle().set("max-width", "100%");
        createdAtField.addValueChangeListener(
                e -> {
                    //TODO сделать фильтрацию
//                    employeeFilter.setCreatedAt(e.getValue().atStartOfDay());
//                    grid.getDataProvider().refreshAll();
//                    grid.refreshPaginator();
                });
    }

    private Component createContent() {
        VerticalLayout content = new VerticalLayout(
                createAddButton(),
                createGrid()
        );

        content.setBoxSizing(BoxSizing.BORDER_BOX);
        content.setHeightFull();
        content.setPadding(false);
        content.setMargin(false);
        content.setSpacing(false);
        return content;
    }

    private void initDataProvider() {
        this.dataProvider = new CallbackDataProvider<Employer, Employer>(
                query -> employerService.find(query, PAGE_SIZE).stream(),
                query -> employerService.getTotalCount(query))
                .withConfigurableFilter();

        this.employerFilter = Employer.builder()
                .status(null)
                .name(null)
                .build();
        this.dataProvider.setFilter(this.employerFilter);
    }

    private Button createAddButton() {
        final Button save = UIUtils.createPrimaryButton("Добавить", VaadinIcon.PLUS);
        save.addClickListener(event -> toViewPage(
                new Employer()
        ));
        return save;
    }


    private Grid<Employer> createGrid() {
        grid = new PaginatedGrid<>();
        grid.setPageSize(PAGE_SIZE);
        grid.setPaginatorSize(2);

        grid.setHeightFull();
        grid.setDataProvider(dataProvider);

        ComponentRenderer<Button, Employer> actionRenderer = new ComponentRenderer<>(
                employer -> {
                    Button editButton = UIUtils.createButton(VaadinIcon.EDIT,
                            ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_SMALL);
                    editButton.addClassName(IconSize.XS.getClassName());
                    editButton.addClickListener(event -> toViewPage(employer));
                    return editButton;
                }
        );
        ComponentRenderer<Badge, Employer> badgeRenderer = new ComponentRenderer<>(
                employer -> {
                    EmployerStatus status = employer.getStatus();
                    Badge badge = new Badge(status.getDescription(), employer.getStatusTheme());
                    return badge;
                }
        );
        Grid.Column<Employer> actionColumn = grid.addColumn(actionRenderer)
                .setFrozen(true)
                .setFlexGrow(0)
                .setWidth("100px")
                .setHeader("Действие")
                .setResizable(true);

        Grid.Column<Employer> idColumn = grid.addColumn(Employer::getId)
                .setAutoWidth(true)
                .setWidth("100px")
                .setHeader("ID")
                .setSortable(true)
                .setComparator(Employer::getId);
        idColumn.setVisible(false);

        Grid.Column<Employer> nameColumn = grid.addColumn(Employer::getName)
                .setWidth("200px")
                .setHeader("Работодатель")
                .setSortable(true)
                .setComparator(Employer::getName)
                .setResizable(true);

        Grid.Column<Employer> statusColumn = grid.addColumn(badgeRenderer)
                .setWidth("200px")
                .setHeader("Статус")
                .setResizable(true)
                .setComparator(Employer::getStatus)
                .setSortable(true);

        Grid.Column<Employer> updatedAtColumn = grid.addColumn(new LocalDateTimeRenderer<>(Employer::getUpdatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setResizable(true)
                .setComparator(Employer::getUpdatedAt)
                .setHeader("Дата обновления");
        updatedAtColumn.setVisible(false);

        Grid.Column<Employer> createdAtColumn = grid.addColumn(new LocalDateTimeRenderer<>(Employer::getCreatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setResizable(true)
                .setComparator(Employer::getCreatedAt)
                .setHeader("Дата создания");

        Button menuButton = new Button();
        menuButton.setIcon(VaadinIcon.ELLIPSIS_DOTS_H.create());
        menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ColumnToggleContextMenu columnToggleContextMenu = new ColumnToggleContextMenu(
                menuButton);
        columnToggleContextMenu.addColumnToggleItem("id", idColumn);
        columnToggleContextMenu.addColumnToggleItem("Работодатель", nameColumn);
        columnToggleContextMenu.addColumnToggleItem("Статус", statusColumn);
        columnToggleContextMenu.addColumnToggleItem("Дата обновления", updatedAtColumn);
        columnToggleContextMenu.addColumnToggleItem("Дата создания", createdAtColumn);

        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();

        headerRow.getCell(actionColumn).setComponent(menuButton);
        headerRow.getCell(idColumn).setComponent(idField);
        headerRow.getCell(nameColumn).setComponent(nameField);
        headerRow.getCell(statusColumn).setComponent(statusField);
        headerRow.getCell(updatedAtColumn).setComponent(updatedAtField);
        headerRow.getCell(createdAtColumn).setComponent(createdAtField);

        return grid;
    }

    private void toViewPage(Employer employer) {
        String param = employer.getId() != null ? employer.getId() : "new";
        UI.getCurrent().navigate(EmployerView.class, param);
    }

    public void withFilter(Employer employerFilter) {
        this.employerFilter = employerFilter;

        binder.setBean(employerFilter);
        binder.bindInstanceFields(this);

        dataProvider.setFilter(employerFilter);
        grid.getDataProvider().refreshAll();
    }
}
