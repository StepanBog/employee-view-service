package tech.inno.odp.backend.data.containers;

import com.vaadin.flow.component.icon.VaadinIcon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.inno.odp.backend.data.enums.CommissionPayer;
import tech.inno.odp.backend.data.enums.EmployerStatus;
import tech.inno.odp.backend.data.enums.PaymentGatewayProvider;
import tech.inno.odp.ui.util.css.lumo.BadgeColor;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employer {

    private String id;

    @Builder.Default
    private String name = "Новый работодатель";

    /**
     * Email работодателя
     */
    private String email;

    /**
     * Статус работодателя
     */
    @Builder.Default
    private EmployerStatus status = EmployerStatus.CREATED;

    /**
     * Платежный метод используемый для
     */
    @Builder.Default
    private PaymentGatewayProvider paymentProvider = PaymentGatewayProvider.REGISTRY;

    /**
     * Процент выдачи
     */
    @Builder.Default
    @Min(value = 0, message = "Не может быть меньше 0")
    private double withdrawalPercentage = 0.60;

    /**
     * Размер комиссии
     * Сумма в копейках. В интерфейсах будет указываться как дробное число в рублях, но хранить нужно целое в копейках
     */
    @Builder.Default
    @Min(value = 0, message = "Не может быть меньше 0")
    private long commissionAmount = 0L;

    /**
     * Плательщик комиссии
     */
    @Builder.Default
    private CommissionPayer commissionPayer = CommissionPayer.EMPLOYER_PAYER;

    /**
     * Минимальный платеж
     */
    @Builder.Default
    @Min(value = 1, message = "Не может быть меньше 1")
    private Long minAmount = 1L;

    /**
     * Максимальный платеж
     */
    @Builder.Default
    @Min(value = 1, message = "Не может быть меньше 1")
    private Long maxAmount = 1L;

    /**
     * Максимальная сумма всех платежей работодателя за 1 календарный месяц с 1-ого числа с 00:00. Ноль означает полную остановку выплат всему работодателю
     */
    @Builder.Default
    @Min(value = 1, message = "Не может быть меньше 1")
    private Long maxMonthlyEmployerTurnover = 100L;

    private Requisites requisites;

    private Tariff tariff;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BadgeColor getStatusTheme() {
        switch (this.status) {
            case CLOSED:
                return BadgeColor.ERROR;
            case ACTIVE:
                return BadgeColor.SUCCESS;
            case PAUSE:
                return BadgeColor.NORMAL;
            default:
                return BadgeColor.CONTRAST;
        }
    }

    public VaadinIcon getStatusIcon() {
        switch (this.status) {
            case CLOSED:
                return VaadinIcon.WARNING;
            case ACTIVE:
                return VaadinIcon.CHECK;
            case PAUSE:
                return VaadinIcon.QUESTION_CIRCLE;
            default:
                return VaadinIcon.QUESTION_CIRCLE;
        }
    }
}
