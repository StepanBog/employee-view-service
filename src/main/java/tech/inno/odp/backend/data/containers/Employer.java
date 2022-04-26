package tech.inno.odp.backend.data.containers;

import com.vaadin.flow.component.icon.VaadinIcon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.inno.odp.backend.data.enums.EmployerStatus;
import tech.inno.odp.backend.data.enums.PaymentGatewayProvider;
import tech.inno.odp.ui.util.css.lumo.BadgeColor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Set;

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
     * Статус работодателя
     */
    @Builder.Default
    @NotNull(message = "Значение не может быть пустым")
    private EmployerStatus status = EmployerStatus.CREATED;

    /**
     * Платежный метод используемый для
     */
    @Builder.Default
    @NotNull(message = "Значение не может быть пустым")
    private PaymentGatewayProvider paymentProvider = PaymentGatewayProvider.NONE_PAYMENT_PROVIDER;

    private Requisites requisites;

    private Set<Tariff> tariffs;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BadgeColor getStatusTheme() {
        switch (this.status) {
            case CLOSED:
                return BadgeColor.ERROR;
            case ACTIVE:
                return BadgeColor.SUCCESS;
            case PAUSE:
                return BadgeColor.NORMAL;
            default:
                return BadgeColor.CONTRAST;
        }
    }

    public VaadinIcon getStatusIcon() {
        switch (this.status) {
            case CLOSED:
                return VaadinIcon.WARNING;
            case ACTIVE:
                return VaadinIcon.CHECK;
            case PAUSE:
                return VaadinIcon.QUESTION_CIRCLE;
            default:
                return VaadinIcon.QUESTION_CIRCLE;
        }
    }
}
