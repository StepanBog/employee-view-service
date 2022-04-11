package tech.inno.odp.backend.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author VKozlov
 * Роли пользователя
 */
@AllArgsConstructor
public enum UserRoleName implements WithDescription {

    ROLE_USER("Пользовательская роль"),
    ROLE_SERVICE("Сервисная роль"),
    ROLE_1C_INTEGRATION("Интеграция с 1С роль"),
    ROLE_TEST_USER("Роль для тестового доступа"),
    ROLE_ADMIN("Администраторская роль");

    @Getter
    private String description;
}
