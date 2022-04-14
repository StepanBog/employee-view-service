package tech.inno.odp.ui.views.employee.form;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import lombok.Setter;
import tech.inno.odp.backend.data.containers.Employee;
import tech.inno.odp.backend.data.containers.ServiceStopInterval;
import tech.inno.odp.backend.service.IServiceStopIntervalService;
import tech.inno.odp.ui.components.grid.PaginatedGrid;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class EmployeeServiceStopIntervalForm extends VerticalLayout {

    public static final String ID = "employeeServiceStopIntervalForm";
    private final int PAGE_SIZE = 15;

    private PaginatedGrid<ServiceStopInterval> grid;

    @Setter
    private IServiceStopIntervalService serviceStopIntervalService;

    public void init() {
        setId(ID);
        setSizeFull();
        add(createGrid());
    }

    private Grid<ServiceStopInterval> createGrid() {
        grid = new PaginatedGrid<>();
        grid.setPageSize(PAGE_SIZE);
        grid.setPaginatorSize(2);
        grid.setHeightFull();

        grid.addColumn(ServiceStopInterval::getId)
                .setAutoWidth(true)
                .setHeader("ID");

        grid.addColumn(new LocalDateTimeRenderer<>(ServiceStopInterval::getFrom, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setComparator(ServiceStopInterval::getFrom)
                .setHeader("Дата начала интервала");

        grid.addColumn(new LocalDateTimeRenderer<>(ServiceStopInterval::getTo, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setComparator(ServiceStopInterval::getTo)
                .setHeader("Дата конца интервала");


        grid.addColumn(new LocalDateTimeRenderer<>(ServiceStopInterval::getUpdatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(ServiceStopInterval::getUpdatedAt)
                .setHeader("Дата обновления");

        grid.addColumn(new LocalDateTimeRenderer<>(ServiceStopInterval::getCreatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(ServiceStopInterval::getCreatedAt)
                .setHeader("Дата создания");

        return grid;
    }

    public void withBean(Employee employee) {
        grid.setDataProvider(DataProvider.ofCollection(
                serviceStopIntervalService.findByEmployeeId(
                        UUID.fromString(employee.getId())
                )
        ));
        grid.getDataProvider().refreshAll();
        grid.refreshPaginator();
    }
}
