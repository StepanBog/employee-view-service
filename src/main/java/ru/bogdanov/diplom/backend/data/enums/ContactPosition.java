package ru.bogdanov.diplom.backend.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Контактные лица раюотодателя
 */
@AllArgsConstructor
public enum ContactPosition implements WithDescription {
    MANAGER("Менеджер"),
    EMPLOYERS_CONTACT("Контактное лицо работодателя");

    @Getter
    private String description;
}
