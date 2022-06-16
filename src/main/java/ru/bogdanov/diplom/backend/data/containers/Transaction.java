package ru.bogdanov.diplom.backend.data.containers;

import com.vaadin.flow.component.icon.VaadinIcon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.bogdanov.diplom.backend.data.enums.TransactionStatus;
import ru.bogdanov.diplom.ui.util.css.lumo.BadgeColor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class Transaction {

    private String id;

    /**
     * Статус транзакции
     */
    private TransactionStatus status;

    /**
     * Сумма транзакции
     */
    private BigDecimal totalSum;

    /**
     * Идентификатор работника
     */
    private String employeeId;

    /**
     * Идентификатор работодателя
     */
    private String employerId;

    /**
     * Дата выполнения транзакции
     */
    private LocalDateTime date;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public BadgeColor getStatusTheme() {
        switch (this.status) {
            case EXPIRED:
            case DECLINE:
                return BadgeColor.ERROR;
            case CONFIRMED:
                return BadgeColor.SUCCESS;
            case WITHDRAWN:
                return BadgeColor.NORMAL;
            default:
                return BadgeColor.CONTRAST;
        }
    }

    public VaadinIcon getIcon() {
        switch (this.status) {
            case EXPIRED:
            case DECLINE:
                return VaadinIcon.WARNING;
            case CONFIRMED:
                return VaadinIcon.CHECK;
            case WITHDRAWN:
            default:
                return VaadinIcon.QUESTION_CIRCLE;
        }
    }
}
