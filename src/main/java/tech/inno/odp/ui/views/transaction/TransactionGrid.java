package tech.inno.odp.ui.views.transaction;

import com.vaadin.flow.component.Component;
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
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import tech.inno.odp.backend.data.containers.Transaction;
import tech.inno.odp.backend.data.enums.TransactionStatus;
import tech.inno.odp.backend.data.enums.WithDescription;
import tech.inno.odp.backend.service.ITransactionService;
import tech.inno.odp.ui.components.Badge;
import tech.inno.odp.ui.components.grid.PaginatedGrid;
import tech.inno.odp.ui.util.UIUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionGrid extends VerticalLayout {

    private final int PAGE_SIZE = 20;

    @Setter
    private ITransactionService transactionService;

    private PaginatedGrid<Transaction> grid;
    private ConfigurableFilterDataProvider<Transaction, Void, Transaction> dataProvider;
    @Getter
    private Transaction transactionFilter;

    public void init() {
        setSizeFull();
        add(createContent());
    }

    private Component createContent() {
        initDataProvider();

        VerticalLayout content = new VerticalLayout(
                createGrid()
        );

        content.setBoxSizing(BoxSizing.BORDER_BOX);
        content.setHeightFull();
        content.setPadding(true);
        return content;
    }

    private void initDataProvider() {
        this.dataProvider = new CallbackDataProvider<Transaction, Transaction>(
                query -> transactionService.find(query, PAGE_SIZE).stream(),
                query -> transactionService.getTotalCount(query))
                .withConfigurableFilter();

        this.transactionFilter = Transaction.builder()
                .status(null)
                .build();
        this.dataProvider.setFilter(this.transactionFilter);
    }


    private Grid<Transaction> createGrid() {
        grid = new PaginatedGrid<>();
        grid.setPageSize(50);
        grid.addSelectionListener(event -> event.getFirstSelectedItem().ifPresent(this::toViewPage));
        grid.setDataProvider(dataProvider);
        grid.setSizeFull();

        ComponentRenderer<Badge, Transaction> badgeRenderer = new ComponentRenderer<>(
                transaction -> {
                    TransactionStatus status = transaction.getStatus();
                    Badge badge = new Badge(status.getDescription(), transaction.getStatusTheme());
                    return badge;
                }
        );
        Grid.Column<Transaction> idColumn = grid.addColumn(Transaction::getId)
                .setWidth("200px")
                .setHeader("ID");

        Grid.Column<Transaction> statusColumn = grid.addColumn(badgeRenderer)
                .setAutoWidth(true)
                .setWidth("200px")
                .setHeader("Статус")
                .setSortable(true);

        Grid.Column<Transaction> totalSumColumn = grid.addColumn(new ComponentRenderer<>(this::createAmount))
                .setWidth("200px")
                .setHeader("Общая сумма")
                .setSortable(true);

        Grid.Column<Transaction> updatedAtColumn = grid.addColumn(new LocalDateTimeRenderer<>(Transaction::getUpdatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Transaction::getUpdatedAt)
                .setHeader("Дата обновления");

        Grid.Column<Transaction> createdAtColumn = grid.addColumn(new LocalDateTimeRenderer<>(Transaction::getCreatedAt, DateTimeFormatter.ofPattern("YYYY dd MMM HH:mm:ss")))
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setComparator(Transaction::getCreatedAt)
                .setHeader("Дата создания");

        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();

        headerRow.getCell(idColumn).setComponent(
                createTextFieldFilterHeader("ID", name -> {
                    transactionFilter.setId(StringUtils.isEmpty(name) ? null : name);
                    grid.getDataProvider().refreshAll();
                    grid.refreshPaginator();
                }));

        headerRow.getCell(statusColumn).setComponent(
                createComboBoxFilterHeader("Статус",
                        Stream.of(TransactionStatus.values()).collect(Collectors.toList()),
                        s -> {
                            transactionFilter.setStatus(s);
                            grid.getDataProvider().refreshAll();
                            grid.refreshPaginator();
                        }));

        headerRow.getCell(updatedAtColumn).setComponent(
                createCDataPickerFilterHeader("Дата обновления", name -> {
                    transactionFilter.setUpdatedAt(name.atStartOfDay());
                    grid.getDataProvider().refreshAll();
                    grid.refreshPaginator();
                }));

        headerRow.getCell(createdAtColumn).setComponent(
                createCDataPickerFilterHeader("Дата создания", name -> {
                    transactionFilter.setCreatedAt(name.atStartOfDay());
                    grid.getDataProvider().refreshAll();
                    grid.refreshPaginator();
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


    private Component createAmount(Transaction transaction) {
        Double amount = transaction.getTotalSum().movePointLeft(2).doubleValue();
        return UIUtils.createAmountLabel(amount);
    }

    private void toViewPage(Transaction transaction) {
//        UI.getCurrent().navigate(AccountDetails.class, bankAccount.getId());
    }
}
