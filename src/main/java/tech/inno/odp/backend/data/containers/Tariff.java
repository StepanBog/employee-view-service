package tech.inno.odp.backend.data.containers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
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
}
