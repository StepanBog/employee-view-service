package ru.bogdanov.diplom.backend.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author SBogdanov
 * Статус работника
 */
@AllArgsConstructor
public enum EmployeeStatus implements WithDescription {

    NEW_EMPLOYEE("Новый статус работника"),
    SIGN_OFFER("Ожидает подписания"),
    ENABLED_EMPLOYEE("Работник готов к работе"),
    DISABLED("Работник отключен"),
    ARCHIVED("Работник в архиве"),
    APPROVED("Работник подтвержден"),
    ERROR("Ошибка"),
    ACT_AVAILABLE_CASH("ACT_AVAILABLE_CASH");

    @Getter
    private String description;
}
