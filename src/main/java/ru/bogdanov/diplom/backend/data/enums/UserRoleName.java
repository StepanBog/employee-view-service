package ru.bogdanov.diplom.backend.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author SBogdanov
 * Роли пользователя
 */
@AllArgsConstructor
public enum UserRoleName implements WithDescription {

    ROLE_EMPLOYEE("Роль рабочего"),
    ROLE_EMPLOYER("Роль работодателя"),
    ROLE_ADMIN("Администраторская роль");

    @Getter
    private String description;
}
