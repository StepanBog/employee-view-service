package tech.inno.odp.backend.data.containers;

import com.vaadin.flow.component.icon.VaadinIcon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.inno.odp.backend.data.enums.EmployeeStatus;
import tech.inno.odp.ui.util.css.lumo.BadgeColor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
     * Позиция работника
     */
    private Position position;

    /**
     * Реквизиты работника
     */
    private Requisites requisites;

    /**
     * Интервалы дней приостановки обслуживания
     */
    private List<ServiceStopInterval> serviceStopIntervals = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BadgeColor getStatusTheme() {
        switch (this.status) {
            case DISABLED:
            case ERROR:
                return BadgeColor.ERROR;
            case ENABLED:
                return BadgeColor.SUCCESS;
            case NEW:
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
            case ENABLED:
                return VaadinIcon.CHECK;
            case NEW:
                return VaadinIcon.QUESTION_CIRCLE;
            default:
                return VaadinIcon.QUESTION_CIRCLE;
        }
    }
}
