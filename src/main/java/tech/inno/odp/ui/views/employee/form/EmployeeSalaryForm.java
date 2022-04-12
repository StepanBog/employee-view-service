package tech.inno.odp.ui.views.employee.form;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import lombok.Getter;
import lombok.Setter;
import tech.inno.odp.backend.data.containers.Employee;
import tech.inno.odp.backend.data.containers.Salary;
import tech.inno.odp.backend.service.ISalaryService;
import tech.inno.odp.ui.components.field.CustomTextField;
import tech.inno.odp.ui.components.grid.PaginatedGrid;
import tech.inno.odp.ui.util.LumoStyles;
import tech.inno.odp.ui.util.UIUtils;
import tech.inno.odp.ui.util.converter.LocalDateToLocalDateTimeConverter;
import tech.inno.odp.ui.util.converter.StringToStringWithNullValueConverter;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * @author VKozlov
 */
public class EmployeeSalaryForm extends VerticalLayout {

    public static final String ID = "employeeSalaryForm";
    private final int PAGE_SIZE = 15;

    @Setter
    private ISalaryService salaryService;
    private PaginatedGrid<Salary> grid;

    @PropertyId("id")
    private CustomTextField idField = new CustomTextField("ID");
    @PropertyId("availableCash")
    private TextField availableCashField = new TextField("Доступная сумма");
    @PropertyId("earnedForMonth")
    private TextField earnedForMonthField = new TextField("Заработанно за месяц");
    @PropertyId("rate")
    private TextField rateField = new TextField("Ставка");

    @PropertyId("updatedAt")
    private DatePicker updatedAtField = new DatePicker("Дата обновления");
    @PropertyId("createdAt")
    private DatePicker createdAtField = new DatePicker("Дата создания");

    @Getter
    private BeanValidationBinder<Salary> binder;
    private final FormLayout editSalaryFormLayout = new FormLayout();

    private Salary salary;

    public void init() {
        setId(ID);
        setSizeFull();

        initFields();

        this.salary = Salary.builder()
                .build();

        this.binder = new BeanValidationBinder<>(Salary.class);
        this.binder.setBean(this.salary);

        LocalDateToLocalDateTimeConverter localDateTimeConverter = new LocalDateToLocalDateTimeConverter();
        StringToLongConverter stringToLongConverter = new StringToLongConverter("");

        this.binder.forField(availableCashField)
                .withConverter(stringToLongConverter)
                .withNullRepresentation(0L)
                .bind(Salary::getAvailableCash, Salary::setAvailableCash);
        this.binder.forField(earnedForMonthField)
                .withConverter(stringToLongConverter)
                .withNullRepresentation(0L)
                .bind(Salary::getEarnedForMonth, Salary::setEarnedForMonth);
        this.binder.forField(rateField)
                .withConverter(stringToLongConverter)
                .withNullRepresentation(0L)
                .bind(Salary::getRate, Salary::setRate);

        this.binder.forField(updatedAtField)
                .withConverter(localDateTimeConverter)
                .bind(Salary::getUpdatedAt, Salary::setUpdatedAt);
        this.binder.forField(createdAtField)
                .withConverter(localDateTimeConverter)
                .bind(Salary::getCreatedAt, Salary::setCreatedAt);
        this.binder.bindInstanceFields(this);

        HorizontalLayout layout = new HorizontalLayout(
                createForm(),
                createGrid()
        );
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setSizeFull();
        add(layout);
    }

    private void initFields() {

        StringToStringWithNullValueConverter stringWithNullValueConverter = new StringToStringWithNullValueConverter();
        idField.setConverters(stringWithNullValueConverter);
        idField.setPlaceholder("ID");
        idField.setReadOnly(true);

        updatedAtField.setReadOnly(true);
        createdAtField.setReadOnly(true);
    }

    public FormLayout createForm() {

        editSalaryFormLayout.add(new Label("Выбранный платеж"));

        editSalaryFormLayout.add(idField);
        editSalaryFormLayout.add(availableCashField);
        editSalaryFormLayout.add(earnedForMonthField);
        editSalaryFormLayout.add(rateField);

        editSalaryFormLayout.add(updatedAtField);
        editSalaryFormLayout.add(createdAtField);
        editSalaryFormLayout.add(createControlButtons());

        editSalaryFormLayout.addClassNames(
                LumoStyles.Padding.Bottom.XS,
                LumoStyles.Padding.Horizontal.XS,
                LumoStyles.Padding.Top.XS);
        editSalaryFormLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP)
        );
        editSalaryFormLayout.setMinWidth("300px");
        
        return editSalaryFormLayout;
    }

    private void gridSelectAction(Salary salary) {
        binder.setBean(salary);
        binder.bindInstanceFields(this);
    }

    private VerticalLayout createGrid() {
        grid = new PaginatedGrid<>();
        grid.setPageSize(PAGE_SIZE);
        grid.setPaginatorSize(2);
        grid.setHeightFull();
        grid.addSelectionListener(event -> event.getFirstSelectedItem().ifPresent(this::gridSelectAction));

        grid.addColumn(Salary::getId)
                .setAutoWidth(true)
                .setHeader("ID");

        Grid.Column<Salary> availableCashColumn = grid.addColumn(
                new ComponentRenderer<>(
                        salary -> {
                            Double amount = salary.getAvailableCash() == null ? 0 : BigDecimal.valueOf(salary.getAvailableCash()).movePointLeft(2).doubleValue();
                            return UIUtils.createAmountLabel(amount);
                        }
                ))
                .setWidth("200px")
                .setComparator(Salary::getAvailableCash)
                .setHeader("Доступная сумма")
                .setSortable(true);


        Grid.Column<Salary> earnedForMonthColumn = grid.addColumn(
                new ComponentRenderer<>(
                        salary -> {
                            Double amount = salary.getEarnedForMonth() == null ? 0 : BigDecimal.valueOf(salary.getEarnedForMonth()).movePointLeft(2).doubleValue();
                            return UIUtils.createAmountLabel(amount);
                        }
                ))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Salary::getEarnedForMonth)
                .setHeader("Заработанно за месяц")
                .setSortable(true);

        Grid.Column<Salary> rateColumn = grid.addColumn(
                new ComponentRenderer<>(
                        salary -> {
                            Double amount = salary.getRate() == null ? 0 : BigDecimal.valueOf(salary.getRate()).movePointLeft(2).doubleValue();
                            return UIUtils.createAmountLabel(amount);
                        }
                ))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Salary::getRate)
                .setHeader("Ставка")
                .setSortable(true);


        grid.addColumn(new LocalDateTimeRenderer<>(Salary::getPeriod, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Salary::getPeriod)
                .setHeader("Период");

        grid.addColumn(new LocalDateTimeRenderer<>(Salary::getUpdatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Salary::getUpdatedAt)
                .setHeader("Дата обновления");

        grid.addColumn(new LocalDateTimeRenderer<>(Salary::getCreatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Salary::getCreatedAt)
                .setHeader("Дата создания");

        VerticalLayout layout = new VerticalLayout(grid);
        return layout;

    }

    public void withBean(Employee employee) {
        List<Salary> listOfSalary = salaryService.findByEmployeeId(UUID.fromString(employee.getId()));
        listOfSalary.forEach(s -> s.setPosition(employee.getPosition()));

        grid.setDataProvider(
                DataProvider.ofCollection(listOfSalary));
        grid.getDataProvider().refreshAll();
        grid.refreshPaginator();
    }

    private HorizontalLayout createControlButtons() {
        final Button search = UIUtils.createPrimaryButton("Сохранить");
        search.addClickListener(event -> {
            Salary salary = binder.getBean();
            binder.setBean(salaryService.save(salary));
            binder.bindInstanceFields(this);
        });

        final Button clear = UIUtils.createTertiaryButton("Очистить");
        clear.addClickListener(event -> {
            Salary salary = Salary.builder().build();
            binder.setBean(salary);
            binder.bindInstanceFields(this);
            grid.deselectAll();
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(search, clear);
        buttonLayout.setSpacing(true);
        buttonLayout.addClassName(LumoStyles.Margin.Top.L);
        return buttonLayout;
    }

}
