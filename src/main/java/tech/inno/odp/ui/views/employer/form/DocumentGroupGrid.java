package tech.inno.odp.ui.views.employer.form;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.data.containers.document.DocumentTemplateGroup;
import tech.inno.odp.backend.data.enums.WithDescription;
import tech.inno.odp.backend.service.IDocumentTemplateService;
import tech.inno.odp.ui.components.grid.PaginatedGrid;
import tech.inno.odp.ui.util.UIUtils;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;


public class DocumentGroupGrid extends VerticalLayout {

    public static final String ID = "documentGroupGrid";

    private final int PAGE_SIZE = 15;

    private PaginatedGrid<DocumentTemplateGroup> grid;
    private ConfigurableFilterDataProvider<DocumentTemplateGroup, Void, DocumentTemplateGroup> dataProvider;
    private DocumentTemplateGroup documentTemplateGroupFilter;

    @Setter
    private IDocumentTemplateService documentTemplateService;
    @Setter
    private Employer employer = new Employer();
    @Getter
    private List<DocumentTemplateGroup> groupList = new ArrayList<>();

    public void init() {
        setId(ID);

        setSizeFull();
        initDataProvider();
        add(createAddButton());
        add(createGrid());
    }

    private void initDataProvider() {
        ConfigurableFilterDataProvider<DocumentTemplateGroup, Void, DocumentTemplateGroup> dataProvider = new CallbackDataProvider<DocumentTemplateGroup, DocumentTemplateGroup>(
                query -> StringUtils.isEmpty(employer.getId()) ?
                        groupList.stream() :
                        documentTemplateService.find(query, PAGE_SIZE).stream(),
                query -> StringUtils.isEmpty(employer.getId()) ? groupList.size() : documentTemplateService.getTotalCount(query))
                .withConfigurableFilter();

        this.dataProvider = dataProvider;
    }

    private Grid<DocumentTemplateGroup> createGrid() {
        grid = new PaginatedGrid<>();
        grid.addSelectionListener(event ->
                createDialog(
                        event.getFirstSelectedItem()
                                .orElse(DocumentTemplateGroup.builder()
                                        .templates(Collections.emptyList())
                                        .build())
                ).open()
        );
        grid.setPageSize(PAGE_SIZE);
        grid.setPaginatorSize(2);
        grid.setSizeFull();

        grid.setDataProvider(dataProvider);

        Grid.Column<DocumentTemplateGroup> nameColumn = grid.addColumn(DocumentTemplateGroup::getName)
                .setFrozen(false)
                .setWidth("300px")
                .setHeader("Название набора документов")
                .setSortable(true);

        grid.addColumn(new LocalDateTimeRenderer<>(DocumentTemplateGroup::getUpdatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(DocumentTemplateGroup::getUpdatedAt)
                .setHeader("Дата обновления");

        grid.addColumn(new LocalDateTimeRenderer<>(DocumentTemplateGroup::getCreatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(DocumentTemplateGroup::getCreatedAt)
                .setHeader("Дата создания");


        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();

        headerRow.getCell(nameColumn).setComponent(
                createTextFieldFilterHeader("Название", name -> {
                    documentTemplateGroupFilter.setName(StringUtils.isEmpty(name) ? null : name);
                    grid.getDataProvider().refreshAll();
                    grid.refreshPaginator();
                }));

        return grid;
    }

    private TextField createTextFieldFilterHeader(String placeHolder,
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

    private Dialog createDialog(DocumentTemplateGroup templateGroup) {
        DocumentGroupDialog templateForm = new DocumentGroupDialog();
        templateForm.setTemplateGroup(templateGroup);
        templateForm.setSaveAction(group -> {
            if (!StringUtils.isEmpty(employer.getId())) {
                documentTemplateService.save(group);
            } else {
                groupList.add(templateGroup);
            }
            dataProvider.refreshAll();
        });
        templateForm.setRemoveAction(group -> {
            if (!StringUtils.isEmpty(employer.getId())) {
                documentTemplateService.remove(group);
            } else {
                groupList.remove(templateGroup);
            }
            dataProvider.refreshAll();
        });
        templateForm.setCloseAction(group -> dataProvider.refreshAll());
        templateForm.init();
        return templateForm;
    }

    private Button createAddButton() {
        final Button addItem = UIUtils.createPrimaryButton("Добавить");
        addItem.addClickListener(event ->
                createDialog(
                        DocumentTemplateGroup.builder()
                                .employerId(employer.getId())
                                .build()
                ).open());
        return addItem;
    }

    public void withBean(Employer employer) {
        this.employer = employer;
        documentTemplateGroupFilter = DocumentTemplateGroup.builder()
                .employerId(employer.getId())
                .build();
        dataProvider.setFilter(this.documentTemplateGroupFilter);
        grid.getDataProvider().refreshAll();
    }
}
