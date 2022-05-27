package ru.bogdanov.diplom.backend.data.containers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SBogdanov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Auth {

    /**
     * access токен
     */
    private String accessToken;

    /**
     * refresh токен
     */
    private String refreshToken;

    /**
     * Пользователя
     */
    private User user;
}
