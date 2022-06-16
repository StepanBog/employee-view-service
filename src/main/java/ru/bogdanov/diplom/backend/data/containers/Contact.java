package ru.bogdanov.diplom.backend.data.containers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bogdanov.diplom.backend.data.enums.ContactPosition;

import javax.validation.constraints.Email;
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
    @Pattern(message = "Некорректный номер телефона",
            regexp = "^(\\+7|7|8)?[\\s\\-]?\\(?[489][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}$")
    private String phone;

    /**
     * E-MAIL
     */
    @Email(message = "Некоректный адрес электронной почты")
    private String email;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
