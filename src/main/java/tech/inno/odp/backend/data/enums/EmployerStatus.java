package tech.inno.odp.backend.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author VKozlov
 * Статус работодателя
 */
@AllArgsConstructor
public enum EmployerStatus implements WithDescription {

    CREATED("Новый статус работодателя"),
    SIGNED("Работодатель подписан"),
    ACTIVE("Работодатель готов к работе"),
    PAUSE("Приостановлен"),
    CLOSED("Работодатель закрыт");

    @Getter
    private String description;
}
