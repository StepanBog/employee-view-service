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
    ENABLED_EMPLOYEE("Готов к работе"),
    DISABLED("Работник отключен");

    @Getter
    private String description;
}
