package ru.bogdanov.diplom.backend.data.containers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employer {

    private String id;

    @Builder.Default
    @NotEmpty(message = "Значение не может быть пустым")
    private String name = "Новый работодатель";

    /**
     * Email работодателя
     */
    @Pattern(message = "Неверное значение", regexp = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")
    private String email;

    /**
     * ИНН
     */
    @Digits(message = "Не больше 12 цифр", integer = 12, fraction = 0)
    private String inn;

    /**
     * КПП
     */
    @Digits(message = "Не больше 9 цифр", integer = 9, fraction = 0)
    private String kpp;

    private Requisites requisites;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
