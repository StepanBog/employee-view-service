package tech.inno.odp.backend.data.containers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Стоп интервалы
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceStopInterval {

    private String id;
    
    /**
     * Дата начала интервала (не может быть нулевым)
     */
    private LocalDateTime from;

    /**
     * Дата конца интервала
     */
    private LocalDateTime to;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
