package ru.bogdanov.diplom.backend.data.containers;

import com.vaadin.flow.component.icon.VaadinIcon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bogdanov.diplom.backend.data.enums.EmployeeStatus;
import ru.bogdanov.diplom.ui.util.css.lumo.BadgeColor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

    private String id;

    /**
     * Телефон работодателя
     */
    private String phone;

    /**
     * Имя работника
     */
    private String firstName;

    /**
     * Отчество работника
     */
    private String patronymicName;

    /**
     * Фамилия работника
     */
    private String lastName;

    /**
     * Идентификатор работодателя
     */
    private String employerId;

    /**
     * Имя работодателя
     */
    private String employerName;

    /**
     * Статус работника
     */
    private EmployeeStatus status;

    /**
     * Зарплата работника
     */
    private Salary salary;

    /**
     * Реквизиты работника
     */
    private Requisites requisites;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BadgeColor getStatusTheme() {
        switch (this.status) {
            case DISABLED:
            case ERROR:
                return BadgeColor.ERROR;
            case ENABLED_EMPLOYEE:
                return BadgeColor.SUCCESS;
            case NEW_EMPLOYEE:
                return BadgeColor.NORMAL;
            default:
                return BadgeColor.CONTRAST;
        }
    }

    public VaadinIcon getStatusIcon() {
        switch (this.status) {
            case DISABLED:
            case ERROR:
                return VaadinIcon.WARNING;
            case ENABLED_EMPLOYEE:
                return VaadinIcon.CHECK;
            case NEW_EMPLOYEE:
                return VaadinIcon.QUESTION_CIRCLE;
            default:
                return VaadinIcon.QUESTION_CIRCLE;
        }
    }
}
