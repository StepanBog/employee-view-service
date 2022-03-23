package tech.inno.odp.ui.views.employer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.data.enums.EmployerStatus;
import tech.inno.odp.backend.data.enums.WithDescription;
import tech.inno.odp.backend.service.IEmployerService;
import tech.inno.odp.ui.components.Badge;
import tech.inno.odp.ui.util.UIUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmployerGrid extends VerticalLayout {

    private final int PAGE_SIZE = 20;

    @Setter
    private IEmployerService employerService;

    private Grid<Employer> grid;
    private ConfigurableFilterDataProvider<Employer, Void, Employer> dataProvider;
    private Employer employerFilter;

    public void init() {
        setSizeFull();
        add(createContent());
    }

    private Component createContent() {
        initDataProvider();

        VerticalLayout content = new VerticalLayout(
                createAddButton(),
                createGrid()
        );

        content.setBoxSizing(BoxSizing.BORDER_BOX);
        content.setHeightFull();
        content.setPadding(true);
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
        final Button save = UIUtils.createPrimaryButton("Добавить");
        save.addClickListener(event -> toViewPage(
                new Employer()
        ));
        return save;
    }


    private Grid<Employer> createGrid() {
        grid = new Grid<>();
        grid.addSelectionListener(event -> event.getFirstSelectedItem().ifPresent(this::toViewPage));
        grid.setPageSize(PAGE_SIZE);
//        grid.setPaginatorSize(2);

        grid.setHeightFull();
        grid.setDataProvider(dataProvider);

        ComponentRenderer<Badge, Employer> badgeRenderer = new ComponentRenderer<>(
                employer -> {
                    EmployerStatus status = employer.getStatus();
                    Badge badge = new Badge(status.getDescription(), employer.getStatusTheme());
                    return badge;
                }
        );
        Grid.Column<Employer> idColumn = grid.addColumn(Employer::getId)
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setHeader("ID");

        Grid.Column<Employer> nameColumn = grid.addColumn(Employer::getName)
                .setWidth("200px")
                .setHeader("Работодатель")
                .setSortable(true);

        Grid.Column<Employer> statusColumn = grid.addColumn(badgeRenderer)
                .setWidth("200px")
                .setHeader("Статус");

        Grid.Column<Employer> updatedAtColumn = grid.addColumn(new LocalDateTimeRenderer<>(Employer::getUpdatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Employer::getUpdatedAt)
                .setHeader("Дата обновления");

        Grid.Column<Employer> createdAtColumn = grid.addColumn(new LocalDateTimeRenderer<>(Employer::getCreatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Employer::getCreatedAt)
                .setHeader("Дата создания");


        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();

        headerRow.getCell(idColumn).setComponent(
                createTextFieldFilterHeader("ID", name -> {
                    employerFilter.setId(StringUtils.isEmpty(name) ? null : name);
                    grid.getDataProvider().refreshAll();
//                    grid.refreshPaginator();
                }));

        headerRow.getCell(nameColumn).setComponent(
                createTextFieldFilterHeader("Название", name -> {
                    employerFilter.setName(StringUtils.isEmpty(name) ? null : name);
                    grid.getDataProvider().refreshAll();
//                    grid.refreshPaginator();
                }));

        headerRow.getCell(statusColumn).setComponent(
                createComboBoxFilterHeader("Статус",
                        Stream.of(EmployerStatus.values())
                                .collect(Collectors.toList()),
                        s -> {
                            employerFilter.setStatus(s);
                            grid.getDataProvider().refreshAll();
//                            grid.refreshPaginator();
                        }));

        headerRow.getCell(updatedAtColumn).setComponent(
                createCDataPickerFilterHeader("Дата обновления", name -> {
                    employerFilter.setUpdatedAt(name.atStartOfDay());
                    grid.getDataProvider().refreshAll();
//                    grid.refreshPaginator();
                }));

        headerRow.getCell(createdAtColumn).setComponent(
                createCDataPickerFilterHeader("Дата создания", name -> {
                    employerFilter.setCreatedAt(name.atStartOfDay());
                    grid.getDataProvider().refreshAll();
//                    grid.refreshPaginator();
                }));

        return grid;
    }

    private static TextField createTextFieldFilterHeader(String placeHolder,
                                                         Consumer<String> filterChangeConsumer) {
        TextField textField = new TextField();
        textField.setPlaceholder(placeHolder);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        textField.setWidthFull();
        textField.getStyle().set("max-width", "100%");
        textField.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        return textField;
    }

    private static <T extends WithDescription> ComboBox<T> createComboBoxFilterHeader(String placeHolder,
                                                                                      List<T> items,
                                                                                      Consumer<T> filterChangeConsumer) {
        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.setItems(items);
        comboBox.setItemLabelGenerator(T::getDescription);

        comboBox.setPlaceholder(placeHolder);
        comboBox.setClearButtonVisible(true);
        comboBox.setWidthFull();
        comboBox.getStyle().set("max-width", "100%");
        comboBox.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        return comboBox;
    }

    private static DatePicker createCDataPickerFilterHeader(String placeHolder,
                                                            Consumer<LocalDate> filterChangeConsumer) {
        DatePicker datePicker = new DatePicker();

        datePicker.setPlaceholder(placeHolder);
        datePicker.setClearButtonVisible(true);
        datePicker.setWidthFull();
        datePicker.getStyle().set("max-width", "100%");
        datePicker.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        return datePicker;
    }

    private void toViewPage(Employer employer) {
        String param = employer.getId() != null ? employer.getId() : "new";
        UI.getCurrent().navigate(EmployerView.class, param);
    }
}
