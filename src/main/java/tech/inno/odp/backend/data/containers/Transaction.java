package tech.inno.odp.backend.data.containers;

import com.vaadin.flow.component.icon.VaadinIcon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import tech.inno.odp.backend.data.enums.TransactionStatus;
import tech.inno.odp.ui.util.css.lumo.BadgeColor;

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
     * Дата выполнения транзакции
     */
    private LocalDateTime date;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public BadgeColor getStatusTheme() {
        switch (this.status) {
            case EXPIRED:
            case DECLINE:
            case TRANSACTION_ERROR:
                return BadgeColor.ERROR;
            case SUCCESS:
                return BadgeColor.SUCCESS;
            case PROCESSING:
                return BadgeColor.NORMAL;
            default:
                return BadgeColor.CONTRAST;
        }
    }

    public VaadinIcon getIcon() {
        switch (this.status) {
            case EXPIRED:
            case DECLINE:
            case TRANSACTION_ERROR:
                return VaadinIcon.WARNING;
            case SUCCESS:
                return VaadinIcon.CHECK;
            case PROCESSING:
                return VaadinIcon.QUESTION_CIRCLE;
            default:
                return VaadinIcon.QUESTION_CIRCLE;
        }
    }
}
