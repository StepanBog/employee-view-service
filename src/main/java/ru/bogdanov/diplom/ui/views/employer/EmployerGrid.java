package ru.bogdanov.diplom.ui.views.employer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import lombok.Setter;
import ru.bogdanov.diplom.backend.data.containers.Employer;
import ru.bogdanov.diplom.backend.service.IEmployerService;
import ru.bogdanov.diplom.ui.components.ColumnToggleContextMenu;
import ru.bogdanov.diplom.ui.components.grid.PaginatedGrid;
import ru.bogdanov.diplom.ui.util.IconSize;
import ru.bogdanov.diplom.ui.util.UIUtils;

import java.time.format.DateTimeFormatter;

public class EmployerGrid  extends VerticalLayout {


    public static final String ID = "employerGrid";
    protected final int PAGE_SIZE = 15;

    @Setter
    protected IEmployerService employerService;

    protected PaginatedGrid<Employer> grid;
    protected ConfigurableFilterDataProvider<Employer, Void, Employer> dataProvider;
    protected Employer employerFilter;

    public void init() {
        setId(ID);
        setSizeFull();
        initDataProvider();
        add(createContent());
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
        columnToggleContextMenu.addColumnToggleItem("Дата обновления", updatedAtColumn);
        columnToggleContextMenu.addColumnToggleItem("Дата создания", createdAtColumn);

        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.getHeaderRows().get(0);

        headerRow.getCell(actionColumn).setComponent(menuButton);

        return grid;
    }

    private void toViewPage(Employer employer) {
        String param = employer.getId() != null ? employer.getId() : "new";
        UI.getCurrent().navigate(EmployerView.class, param);
    }

    public void withFilter(Employer employerFilter) {
        this.employerFilter = employerFilter;
        dataProvider.setFilter(employerFilter);
        grid.getDataProvider().refreshAll();
    }
}
