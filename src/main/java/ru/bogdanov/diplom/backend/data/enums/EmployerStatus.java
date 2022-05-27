package ru.bogdanov.diplom.backend.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author SBogdanov
 * Статус работодателя
 */
@AllArgsConstructor
public enum EmployerStatus implements WithDescription {

    NONE_STATUS("Отсутствует"),
    CREATED("Новый статус работодателя"),
    SIGNED("Работодатель подписан"),
    ACTIVE("Работодатель готов к работе"),
    PAUSE("Приостановлен"),
    CLOSED("Работодатель закрыт");

    @Getter
    private String description;
}
