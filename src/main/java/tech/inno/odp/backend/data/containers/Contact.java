package tech.inno.odp.backend.data.containers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.inno.odp.backend.data.enums.ContactPosition;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contact {

    private String id;

    /**
     * Должность контактного лица
     */
    private ContactPosition position;

    /**
     * ФИО контактного лица
     */
    private String name;

    /**
     * Телефон контактного лица
     */
    private String phone;

    /**
     * E-MAIL
     */
    private String email;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
