package ru.bogdanov.diplom.ui.views.transaction.form;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.PropertyId;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.bogdanov.diplom.backend.data.containers.Requisites;
import ru.bogdanov.diplom.backend.data.containers.Transaction;
import ru.bogdanov.diplom.backend.data.enums.TransactionStatus;
import ru.bogdanov.diplom.backend.service.ITransactionService;
import ru.bogdanov.diplom.ui.util.LumoStyles;
import ru.bogdanov.diplom.ui.util.UIUtils;

import java.util.function.Consumer;

@Slf4j
public class DeclineTransactionDialog extends Dialog {

    @Getter
    private BeanValidationBinder<Transaction> binderTransaction;

    @Setter
    private Consumer<Transaction> declineAction;
    @Setter
    private Consumer<Transaction> cancelAction;

    private Transaction transaction;

    @PropertyId("totalSum")
    private BigDecimalField totalSum = new BigDecimalField("Сумма запроса");


    private FormLayout transactionLayout = new FormLayout();

    @Setter
    private ITransactionService transactionService;

    private Button decline,cancel;


    public void init() {
        setWidth("60%");

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        this.transaction = Transaction.builder().build();
        this.binderTransaction = new BeanValidationBinder<>(Transaction.class);
        this.binderTransaction.setBean(this.transaction);
        this.binderTransaction.bindInstanceFields(this);

        VerticalLayout layout = new VerticalLayout();

        layout.add(createForm());
        layout.add(createButtons());
        add(layout);
    }
    private Component createAmount(Transaction transaction) {
        Double amount = transaction.getTotalSum().movePointLeft(2).doubleValue();
        return UIUtils.createAmountLabel(amount);
    }

    public VerticalLayout createForm() {
        FormLayout dataLayout = new FormLayout();
        dataLayout.addClassNames(LumoStyles.Padding.Bottom.XS,
                LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.XS);
        dataLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("600px", 2,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("1024px", 2,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP));

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setPadding(false);
        verticalLayout.setMargin(false);
        verticalLayout.setSpacing(false);


        transactionLayout.add(totalSum);
        transactionLayout.addClassNames(LumoStyles.Padding.Bottom.XS,
                LumoStyles.Padding.Horizontal.L, LumoStyles.Padding.Top.XS);
        transactionLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("600px", 1,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP),
                new FormLayout.ResponsiveStep("1024px", 3,
                        FormLayout.ResponsiveStep.LabelsPosition.TOP));

        verticalLayout.add(dataLayout);
        verticalLayout.add(transactionLayout);
        return verticalLayout;
    }


    private HorizontalLayout createButtons() {
        HorizontalLayout layout = new HorizontalLayout();

        decline = UIUtils.createRedButton("Отозвать");
        decline.addClickListener(event -> {
            Transaction transaction = this.binderTransaction.getBean();
            transaction.setStatus(TransactionStatus.WITHDRAWN);
            transactionService.update(transaction);
            declineAction.accept(transaction);
            this.close();
        });

       cancel = UIUtils.createTertiaryButton("Отменить");
       cancel.addClickListener(event -> {
            cancelAction.accept(transaction);

            this.close();
        });
        layout.add(decline);
        layout.add(cancel);
        return layout;
    }
    public void withBean(Transaction transaction) {
        this.binderTransaction.removeBean();
        this.binderTransaction.setBean(transaction);
        this.binderTransaction.bindInstanceFields(this);
        if (!transaction.getStatus().name().equals(TransactionStatus.AWAITING_CONFORMATION.name())) {
            decline.setVisible(false);
        }
        totalSum.setValue(totalSum.getValue().movePointLeft(2));
        totalSum.setReadOnly(true);
    }

}
