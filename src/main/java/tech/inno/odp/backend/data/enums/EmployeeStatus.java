package tech.inno.odp.backend.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author VKozlov
 * Статус работника
 */
@AllArgsConstructor
public enum EmployeeStatus implements WithDescription {

    NEW("Новый статус работника"),
    SIGN_OFFER("Ожидает подписания"),
    ENABLED("Работник готов к работе"),
    DISABLED("Работник отключен"),
    ARCHIVED("Работник в архиве"),
    APPROVED("Работник подтвержден"),
    ERROR("Ошибка"),
    OFFER_SIGNED("Подписан"),
    ACT_AVAILABLE_CASH("ACT_AVAILABLE_CASH");

    @Getter
    private String description;
}
