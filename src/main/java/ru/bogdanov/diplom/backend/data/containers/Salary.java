package ru.bogdanov.diplom.backend.data.containers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author SBogdanov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Salary {

    private String id;

    /**
     * Доступная сумма
     */
    private Long availableCash;

    /**
     * Заработанно за месяц
     */
    private Long earnedForMonth;

    /**
     * Ставка
     */
    private Long rate;

    private LocalDateTime period;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
