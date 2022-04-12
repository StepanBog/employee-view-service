package tech.inno.odp.ui.views.transaction.form;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.provider.DataProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.data.containers.Transaction;
import tech.inno.odp.backend.data.enums.TransactionStatus;
import tech.inno.odp.backend.service.IEmployerService;
import tech.inno.odp.grpc.generated.service.employer.SearchEmployerRequest;
import tech.inno.odp.ui.util.LumoStyles;
import tech.inno.odp.ui.util.UIUtils;
import tech.inno.odp.ui.util.converter.LocalDateToLocalDateTimeConverter;
import tech.inno.odp.ui.views.transaction.TransactionGrid;

/**
 * @author VKozlov
 */
@RequiredArgsConstructor
public class TransactionSearchInGridForm extends VerticalLayout {

    public static final String ID = "transactionSearchForm";

    private final IEmployerService employerService;
    private final TransactionGrid grid;

    @PropertyId("id")
    private TextField idField = new TextField();
    @PropertyId("status")
    private ComboBox<TransactionStatus> statusField = new ComboBox();
    @PropertyId("totalSum")
    private BigDecimalField totalSumField = new BigDecimalField();

    private ComboBox<Employer> employerField = new ComboBox<>();

    @PropertyId("updatedAt")
    private DatePicker updatedAtField = new DatePicker();
    @PropertyId("createdAt")
    private DatePicker createdAtField = new DatePicker();

    @Getter
    private BeanValidationBinder<Transaction> binder;

    private Transaction transactionFilter;

    public void init() {
        setId(ID);

        initFields();

        this.transactionFilter = Transaction.builder()
                .status(null)
                .build();


        this.binder = new BeanValidationBinder<>(Transaction.class);
        this.binder.setBean(this.transactionFilter);

        LocalDateToLocalDateTimeConverter localDateTimeConverter = new LocalDateToLocalDateTimeConverter();
        this.binder.forField(updatedAtField)
                .withConverter(localDateTimeConverter)
                .bind(Transaction::getUpdatedAt, Transaction::setUpdatedAt);
        this.binder.forField(createdAtField)
                .withConverter(localDateTimeConverter)
                .bind(Transaction::getCreatedAt, Transaction::setCreatedAt);
        this.binder.bindInstanceFields(this);

        add(createForm());
    }

    private void initFields() {
        idField.setPlaceholder("ID");
        idField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());

        statusField.setItems(TransactionStatus.values());
        statusField.setItemLabelGenerator(TransactionStatus::getDescription);
        statusField.setPlaceholder("Статус");
        statusField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());

        totalSumField.setPlaceholder("Сумма");
        totalSumField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());

        employerField.setItemLabelGenerator(Employer::getName);
        ComboBox.ItemFilter<Employer> filter = (employer, filterString) ->
                employer.getName().toLowerCase().startsWith(filterString.toLowerCase());
        employerField.setDataProvider(filter, DataProvider.fromStream(
                employerService.findAll(
                        SearchEmployerRequest.newBuilder()
                                .build()
                ).stream()
        ));
        employerField.addValueChangeListener(
                e -> transactionFilter.setEmployerId(e.getValue() != null ? e.getValue().getId() : null));
        employerField.setPlaceholder("Работодатель");
        employerField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());

        updatedAtField.setPlaceholder("Дата обновления");
        updatedAtField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());

        createdAtField.setPlaceholder("Дата создания");
        createdAtField.getElement().getThemeList().add(TextFieldVariant.LUMO_SMALL.getVariantName());
    }

    public FormLayout createForm() {
        FormLayout formLayout = new FormLayout();

        formLayout.add(new Label("Поиск"));

        formLayout.add(idField);
        formLayout.add(statusField);
        formLayout.add(totalSumField);
        formLayout.add(employerField);

        formLayout.add(updatedAtField);
        formLayout.add(createdAtField);

        formLayout.add(createControlButtons());

        formLayout.addClassNames(
                LumoStyles.Padding.Bottom.XS,
                LumoStyles.Padding.Horizontal.XS,
                LumoStyles.Padding.Top.XS);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP)
        );
        return formLayout;
    }

    private HorizontalLayout createControlButtons() {
        final Button search = UIUtils.createPrimaryButton("Поиск");
        search.addClickListener(event -> {
            transactionFilter = binder.getBean();
            grid.withFilter(transactionFilter);
        });

        final Button clear = UIUtils.createTertiaryButton("Очистить");
        clear.addClickListener(event -> {
            transactionFilter = Transaction.builder()
                    .status(null)
                    .build();
            binder.setBean(transactionFilter);
            binder.bindInstanceFields(this);
            employerField.setValue(null);
            grid.withFilter(transactionFilter);
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(search, clear);
        buttonLayout.setSpacing(true);
        buttonLayout.addClassName(LumoStyles.Margin.Top.L);
        return buttonLayout;
    }
}
