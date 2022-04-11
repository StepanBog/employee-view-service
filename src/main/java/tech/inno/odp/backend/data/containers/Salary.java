package tech.inno.odp.backend.data.containers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author VKozlov
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

    /**
     * Позиция
     */
    private Position position;

    private LocalDateTime period;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
