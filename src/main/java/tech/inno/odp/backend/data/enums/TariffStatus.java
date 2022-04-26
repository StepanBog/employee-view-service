package tech.inno.odp.backend.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Статус тарифа
 */
@AllArgsConstructor
public enum TariffStatus implements WithDescription {
    NONE_TARIFF_STATUS("Отсутствует"),
    ACTIVE("Активен"),
    STOP("Приостановлен");

    @Getter
    private String description;
}
