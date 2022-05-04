package tech.inno.odp.backend.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Бюджетная организация
 */
@AllArgsConstructor
public enum BudgetOrganization implements WithDescription {
    NO("Нет"),
    YES("Да");

    @Getter
    private String description;
}
