package tech.inno.odp.backend.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author VKozlov
 */
@AllArgsConstructor
public enum CommissionPayer implements WithDescription {

    UNKNOWN_PAYER("Отсутствует"),
    EMPLOYER_PAYER("Работодатель"),
    EMPLOYEE_PAYER("Работник"),
    BANK_PAYER("Банк"),
    APPLICATION_PAYER("Организатор продукта");

    @Getter
    private String description;
}
