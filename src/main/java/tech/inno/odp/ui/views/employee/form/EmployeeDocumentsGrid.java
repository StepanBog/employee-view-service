package tech.inno.odp.ui.views.employee.form;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.StreamResource;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import tech.inno.odp.backend.data.containers.Employee;
import tech.inno.odp.backend.data.containers.document.Document;
import tech.inno.odp.backend.data.enums.DocumentGroupType;
import tech.inno.odp.backend.data.enums.WithDescription;
import tech.inno.odp.backend.service.IDocumentService;
import tech.inno.odp.ui.components.Badge;
import tech.inno.odp.ui.components.file.FileDownloadWrapper;
import tech.inno.odp.ui.components.grid.PaginatedGrid;
import tech.inno.odp.ui.util.UIUtils;
import tech.inno.odp.ui.util.css.lumo.BadgeColor;

import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class EmployeeDocumentsGrid extends VerticalLayout {

    private final int PAGE_SIZE = 20;

    private PaginatedGrid<Document> grid;
    private ConfigurableFilterDataProvider<Document, Void, Document> dataProvider;
    private Document documentFilter;

    @Setter
    private IDocumentService documentService;
    @Setter
    private Employee employee;

    public void init() {
        setSizeFull();
        initDataProvider();
        add(createGrid());
    }

    private void initDataProvider() {
        ConfigurableFilterDataProvider<Document, Void, Document> dataProvider = new CallbackDataProvider<Document, Document>(
                query -> documentService.find(query, PAGE_SIZE).stream(),
                query -> documentService.getTotalCount(query))
                .withConfigurableFilter();

        this.documentFilter = Document.builder()
                .employerId(employee.getEmployerId())
                .employeeId(employee.getId())
                .build();
        dataProvider.setFilter(this.documentFilter);
        this.dataProvider = dataProvider;
    }

    private Grid<Document> createGrid() {
        grid = new PaginatedGrid<>();
        grid.setPageSize(PAGE_SIZE);
        grid.setPaginatorSize(2);
        grid.setHeightFull();

        grid.setDataProvider(dataProvider);

        ComponentRenderer<Badge, Document> badgeRenderer = new ComponentRenderer<>(
                group -> {
                    DocumentGroupType groupType = group.getGroupType();
                    Badge badge = new Badge(groupType.getDescription(), BadgeColor.NORMAL);
                    return badge;
                }
        );

        Grid.Column<Document> idColumn = grid.addColumn(Document::getId)
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setFrozen(true)
                .setHeader("ID");

        Grid.Column<Document> typeColumn = grid.addColumn(badgeRenderer)
                .setAutoWidth(true)
                .setHeader("Тип набора");

        grid.addColumn(new ComponentRenderer<>(this::createActive))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setHeader("Подписан")
                .setTextAlign(ColumnTextAlign.END);

        grid.addComponentColumn(document -> {
            final Button load = new Button();
            load.setIcon(VaadinIcon.DOWNLOAD.create());
            load.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            StreamResource resource = new StreamResource(
                    document.getId() + "." + document.getAttachment().getExtension(),
                    () -> new ByteArrayInputStream(document.getAttachment().getFile()));
            resource.setContentTypeResolver((streamResource, servletContext) -> document.getAttachment().getContentType());

            FileDownloadWrapper fileDownloadWrapper = new FileDownloadWrapper(resource);
            fileDownloadWrapper.wrapComponent(load);

            return fileDownloadWrapper;
        }).setTextAlign(ColumnTextAlign.CENTER)
                .setHeader("Скачать файл")
                .setAutoWidth(true)
                .setFlexGrow(0);


        grid.addColumn(new LocalDateTimeRenderer<>(Document::getUpdatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Document::getUpdatedAt)
                .setHeader("Дата обновления");

        grid.addColumn(new LocalDateTimeRenderer<>(Document::getCreatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Document::getCreatedAt)
                .setHeader("Дата создания");


        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();

        headerRow.getCell(idColumn).setComponent(
                createTextFieldFilterHeader("ID", name -> {
                    documentFilter.setId(StringUtils.isEmpty(name) ? null : name);
                    grid.getDataProvider().refreshAll();
                    grid.refreshPaginator();
                }));

        headerRow.getCell(typeColumn).setComponent(
                createComboBoxFilterHeader("Тип",
                        Stream.of(DocumentGroupType.values())
                                .collect(Collectors.toList()),
                        s -> {
                            documentFilter.setGroupType(s);
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

    private Component createActive(Document document) {
        Icon icon;
        if (document.getSigned()) {
            icon = UIUtils.createPrimaryIcon(VaadinIcon.CHECK);
        } else {
            icon = UIUtils.createDisabledIcon(VaadinIcon.CLOSE);
        }
        return icon;
    }
}
