package tech.inno.odp.backend.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SpecTariffCondition implements WithDescription {

    NONE_SPEC_TARIFF_CONDITION("Нет условий"),
    BY_DAYS("Учет дней"),
    BY_PAYMENTS("Учет платежей"),
    BY_DAYS_AND_PAYMENTS("Учет дней и платежей одновременно"),
    BY_DAYS_OR_PAYMENTS("Учет дней или платежей");

    @Getter
    private String description;
}
