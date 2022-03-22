package tech.inno.odp.ui.views.employer.form;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.server.StreamResource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tech.inno.odp.backend.data.containers.document.DocumentBody;
import tech.inno.odp.backend.data.containers.document.DocumentTemplate;
import tech.inno.odp.backend.data.containers.document.DocumentTemplateGroup;
import tech.inno.odp.backend.data.enums.DocumentTemplateGroupType;
import tech.inno.odp.ui.components.file.FileDownloadWrapper;
import tech.inno.odp.ui.util.LumoStyles;
import tech.inno.odp.ui.util.UIUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class DocumentTemplateGroupDialog extends Dialog {

    @Getter
    private BeanValidationBinder<DocumentTemplateGroup> binder;

    @Setter
    private Consumer<DocumentTemplateGroup> saveAction;
    @Setter
    private Consumer<DocumentTemplateGroup> closeAction;
    @Setter
    private Consumer<DocumentTemplateGroup> removeAction;
    @Setter
    private DocumentTemplateGroup templateGroup;

    private List<DocumentTemplate> documentTemplateList = new ArrayList<>();
    private Grid<DocumentTemplate> grid;

    private ListDataProvider<DocumentTemplate> dataProvider;

    @PropertyId("name")
    private TextField nameField = new TextField("Имя");
    @PropertyId("type")
    private ComboBox<DocumentTemplateGroupType> typeField = new ComboBox("Тип");

    public void init() {
        initFields();
        setWidth("60%");

        if (StringUtils.hasLength(templateGroup.getId())) {
            this.getElement().setAttribute("aria-label", "Настройка набора документов");
        } else {
            this.getElement().setAttribute("aria-label", "Создание набора документов");
        }

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        this.documentTemplateList = CollectionUtils.isEmpty(this.templateGroup.getTemplates())
                ? new ArrayList<>()
                : this.templateGroup.getTemplates();
        this.binder = new BeanValidationBinder<>(DocumentTemplateGroup.class);
        this.binder.setBean(this.templateGroup);
        this.binder.bindInstanceFields(this);

        VerticalLayout layout = new VerticalLayout();

        layout.add(createForm());
        layout.add(createGrid());
        layout.add(createButtons());
        add(layout);
    }

    private void initFields() {
        typeField.setItems(DocumentTemplateGroupType.values());
        typeField.setItemLabelGenerator(DocumentTemplateGroupType::getDescription);
    }

    public FormLayout createForm() {
        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Upload upload = new Upload(memoryBuffer);

        upload.addSucceededListener(event -> {
            try (InputStream inputStream = memoryBuffer.getInputStream()) {
                String extension = FilenameUtils.getExtension(event.getFileName());

                documentTemplateList.add(
                        DocumentTemplate.builder()
                                .name(event.getFileName().replace("." + extension, ""))
                                .attachment(
                                        DocumentBody.builder()
                                                .contentType(event.getMIMEType())
                                                .file(inputStream.readAllBytes())
                                                .extension(extension)
                                                .build()
                                )
                                .build());
                dataProvider.refreshAll();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        FormLayout layout = new FormLayout();
        layout.add(nameField);
        layout.add(typeField);
        layout.add(upload, 2);

        layout.addClassNames(LumoStyles.Padding.Bottom.L,
                LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.S);
        layout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("400px", 2,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("1024px", 2,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP));
        return layout;
    }

    private Grid<DocumentTemplate> createGrid() {
        grid = new Grid<>();
        grid.setMaxHeight("300px");
        grid.setPageSize(50);
        dataProvider = DataProvider.ofCollection(documentTemplateList);
        grid.setDataProvider(dataProvider);

        grid.addColumn(DocumentTemplate::getName)
                .setAutoWidth(true)
                .setHeader("Название документа")
                .setSortable(false);

        grid.addComponentColumn(template -> {
            final Button load = new Button();
            load.setIcon(VaadinIcon.DOWNLOAD.create());
            load.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            StreamResource resource = new StreamResource(
                    template.getName() + "." + template.getAttachment().getExtension(),
                    () -> new ByteArrayInputStream(template.getAttachment().getFile()));
            resource.setContentTypeResolver((streamResource, servletContext) -> template.getAttachment().getContentType());

            FileDownloadWrapper fileDownloadWrapper = new FileDownloadWrapper(resource);
            fileDownloadWrapper.wrapComponent(load);

            return fileDownloadWrapper;
        }).setAutoWidth(true)
                .setFlexGrow(0);

        grid.addComponentColumn(template -> {
            final Button remove = new Button();
            remove.setIcon(VaadinIcon.FILE_REMOVE.create());
            remove.addThemeVariants(ButtonVariant.LUMO_ERROR);

            remove.addClickListener(event -> {
                documentTemplateList.remove(template);
                dataProvider.refreshAll();
            });

            return remove;
        }).setAutoWidth(true)
                .setFlexGrow(0);

        return grid;
    }

    private HorizontalLayout createButtons() {
        HorizontalLayout layout = new HorizontalLayout();

        final Button save = UIUtils.createPrimaryButton("Сохранить");
        save.addClickListener(event -> {
            if (this.binder.isValid()) {
                this.templateGroup = this.binder.getBean();
                this.templateGroup.setTemplates(new ArrayList<>(dataProvider.getItems()));
                saveAction.accept(this.templateGroup);
                this.close();
            }
        });
        final Button remove = UIUtils.createErrorButton("Удалить", VaadinIcon.TRASH);
        remove.addClickListener(event -> {
            this.binder.removeBean();
            removeAction.accept(this.templateGroup);
            this.close();
        });
        final Button cancel = UIUtils.createTertiaryButton("Отменить");
        cancel.addClickListener(event -> {
            this.binder.removeBean();
            closeAction.accept(this.templateGroup);
            this.close();
        });

        layout.add(save);
        layout.add(remove);
        layout.add(cancel);
        return layout;
    }
}
