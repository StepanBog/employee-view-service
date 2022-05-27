package ru.bogdanov.diplom.ui.views.employee.form;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.bogdanov.diplom.backend.data.containers.Employee;
import ru.bogdanov.diplom.backend.data.containers.Salary;
import ru.bogdanov.diplom.backend.data.containers.Transaction;
import ru.bogdanov.diplom.backend.data.containers.User;
import ru.bogdanov.diplom.backend.service.ISalaryService;
import ru.bogdanov.diplom.backend.service.ITransactionService;
import ru.bogdanov.diplom.ui.components.field.CustomTextField;
import ru.bogdanov.diplom.ui.components.grid.PaginatedGrid;
import ru.bogdanov.diplom.ui.util.LumoStyles;
import ru.bogdanov.diplom.ui.util.UIUtils;
import ru.bogdanov.diplom.ui.util.converter.BigDecimalToLongConverter;
import ru.bogdanov.diplom.ui.util.converter.LocalDateToLocalDateTimeConverter;
import ru.bogdanov.diplom.ui.util.converter.StringToStringWithNullValueConverter;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author SBogdanov
 */
public class EmployeeSalaryForm extends VerticalLayout {

    public static final String ID = "employeeSalaryForm";

    @Setter
    private ISalaryService salaryService;
    @Setter
    private ITransactionService transactionService;

    @PropertyId("availableCash")
    private BigDecimalField availableCashField = new BigDecimalField("Доступная сумма");
    @PropertyId("earnedForMonth")
    private BigDecimalField earnedForMonthField = new BigDecimalField("Заработанно за месяц");
    @PropertyId("rate")
    private BigDecimalField rateField = new BigDecimalField("Ставка");

    private TextField sum = new TextField("Сумма запроса");

    @Getter
    private BeanValidationBinder<Salary> binder;
    private final FormLayout salaryFormLayout = new FormLayout();
    private final FormLayout requestFormLayout = new FormLayout();
    private Salary salary;

    public void init() {
        setId(ID);
        setSizeFull();

        this.salary = Salary.builder()
                .build();

        this.binder = new BeanValidationBinder<>(Salary.class);
        this.binder.setBean(this.salary);

        LocalDateToLocalDateTimeConverter localDateTimeConverter = new LocalDateToLocalDateTimeConverter();
        BigDecimalToLongConverter bigDecimalToLongConverter = new BigDecimalToLongConverter();

        this.binder.forField(availableCashField)
                .withConverter(bigDecimalToLongConverter)
                .withNullRepresentation(0L)
                .bind(Salary::getAvailableCash, Salary::setAvailableCash);
        this.binder.forField(earnedForMonthField)
                .withConverter(bigDecimalToLongConverter)
                .withNullRepresentation(0L)
                .bind(Salary::getEarnedForMonth, Salary::setEarnedForMonth);
        this.binder.forField(rateField)
                .withConverter(bigDecimalToLongConverter)
                .withNullRepresentation(0L)
                .bind(Salary::getRate, Salary::setRate);
        this.binder.bindInstanceFields(this);

        HorizontalLayout layout = new HorizontalLayout(
                createForm()
        );
        HorizontalLayout requestLayout = new HorizontalLayout(
                createRequestForm()
        );
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setSizeFull();
        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.add(layout);
        mainLayout.add(requestLayout);
        add(mainLayout);


        availableCashField.setReadOnly(true);
        rateField.setReadOnly(true);
        earnedForMonthField.setReadOnly(true);
    }
    public FormLayout createForm() {

        salaryFormLayout.add(new Label("Выбранный платеж"));

        salaryFormLayout.add(availableCashField);
        salaryFormLayout.add(earnedForMonthField);
        salaryFormLayout.add(rateField);

        salaryFormLayout.addClassNames(
                LumoStyles.Padding.Bottom.XS,
                LumoStyles.Padding.Horizontal.XS,
                LumoStyles.Padding.Top.XS);
        salaryFormLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP)
        );
        salaryFormLayout.setMinWidth("300px");
        
        return salaryFormLayout;
    }
    public FormLayout createRequestForm() {

        requestFormLayout.add(new Label("Запрос платежа"));
        requestFormLayout.add(sum);
        requestFormLayout.add(createControlButtons());


        requestFormLayout.addClassNames(
                LumoStyles.Padding.Bottom.XS,
                LumoStyles.Padding.Horizontal.XS,
                LumoStyles.Padding.Top.XS);
        requestFormLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP)
        );
        requestFormLayout.setMinWidth("300px");

        return requestFormLayout;
    }
    private HorizontalLayout createControlButtons() {
        final Button request = UIUtils.createPrimaryButton("Создать запрос");
        request.addClickListener(event -> {
            User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Transaction transaction = transactionService.pay(UUID.fromString(user.getEmployeeId()), Long.parseLong(sum.getValue() + "00"));
            availableCashField.setValue(availableCashField.getValue().subtract(transaction.getTotalSum().movePointLeft(2)));
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(request);
        buttonLayout.setSpacing(true);
        buttonLayout.addClassName(LumoStyles.Margin.Top.L);
        return buttonLayout;
    }

    private void gridSelectAction(Salary salary) {
        binder.setBean(salary);
        binder.bindInstanceFields(this);
    }

    public void withBean(Employee employee) {
        Salary salary = salaryService.findByEmployeeId(UUID.fromString(employee.getId()));
        this.availableCashField.setValue(BigDecimal.valueOf(salary.getAvailableCash()).movePointLeft(2));
       // this.availableCashField.setValue(String.valueOf(new BigDecimal(salary.getAvailableCash()).movePointLeft(2)));
        this.earnedForMonthField.setValue(BigDecimal.valueOf(salary.getEarnedForMonth()).movePointLeft(2));
        this.rateField.setValue(BigDecimal.valueOf(salary.getRate()).movePointLeft(2));
    }



}
