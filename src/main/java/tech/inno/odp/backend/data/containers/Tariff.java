package tech.inno.odp.backend.data.containers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.inno.odp.backend.data.enums.CommissionPayer;
import tech.inno.odp.backend.data.enums.PaymentGatewayProvider;
import tech.inno.odp.backend.data.enums.SpecTariffCondition;
import tech.inno.odp.backend.data.enums.TariffStatus;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tariff {

    private String id;
    /**
     * Включение 0% комиссии на время
     */
    @Builder.Default
    private boolean enable = false;

    /**
     * Включение 0% комиссии на время
     */
    @Builder.Default
    private boolean zeroCommissionEnable = false;

    /**
     * Количество льготных дней
     */
    @Builder.Default
    @Min(value = 0, message = "Не может быть меньше 0")
    private Integer preferentialPaymentDays = 0;

    /**
     * Количество льготных платежей
     */
    @Builder.Default
    @Min(value = 0, message = "Не может быть меньше 0")
    private Integer preferentialPaymentCount = 0;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Процент выдачи (по дефолту 60%)
     */
    @Builder.Default
    @Min(value = 0, message = "Не может быть меньше 0")
    private double withdrawalPercentage = 0.0;

    /**
     * Размер комиссии
     * Сумма в копейках. В интерфейсах будет указываться как дробное число в рублях, но хранить нужно целое в копейках
     */
    @Builder.Default
    @Min(value = 0, message = "Не может быть меньше 0")
    private long commissionAmount = 0L;

    /**
     * Плательщик комиссии (по дефолту работодатель)
     */
    @Builder.Default
    @NotNull(message = "Значение не может быть пустым")
    private CommissionPayer commissionPayer = CommissionPayer.UNKNOWN_PAYER;

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
    private Long maxMonthlyEmployerTurnover = 1L;

    /**
     * Статус тарифа
     */
    @Builder.Default
    @NotNull(message = "Значение не может быть пустым")
    private TariffStatus tariffStatus = TariffStatus.NONE_TARIFF_STATUS;

    /**
     * Условия по спецтарифам
     */
    @Builder.Default
    @NotNull(message = "Значение не может быть пустым")
    private SpecTariffCondition specTariffCondition = SpecTariffCondition.NONE_SPEC_TARIFF_CONDITION;

    /**
     * Платежный метод используемый для
     */
    @Builder.Default
    @NotNull(message = "Значение не может быть пустым")
    private PaymentGatewayProvider paymentProvider = PaymentGatewayProvider.NONE_PAYMENT_PROVIDER;

    /**
     * Размер комиссии по спецтарифу
     * Сумма в копейках. В интерфейсах будет указываться как дробное число в рублях, но хранить нужно целое в копейках
     */
    @Builder.Default
    @Min(value = 0, message = "Не может быть меньше 0")
    private long specTariffCommissionAmount = 0L;
}
