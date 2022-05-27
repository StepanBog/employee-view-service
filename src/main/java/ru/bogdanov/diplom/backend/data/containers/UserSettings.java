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
public class UserSettings {

    private String id;

    /**
     * Время жизни токена в секундах (если не заданно то используется дефолтное значение заданное в настройках)
     */
    @Builder.Default
    private Long tokenTtl = 1800L;

    /**
     * Время жизни токена обновления в секундах (если не заданно то используется дефолтное значение заданное в настройках)
     */
    @Builder.Default
    private Long refreshTokenTtl = 172800L;
}
