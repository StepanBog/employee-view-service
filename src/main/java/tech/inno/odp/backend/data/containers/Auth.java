package tech.inno.odp.backend.data.containers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author VKozlov
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
