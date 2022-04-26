package tech.inno.odp.backend.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author VKozlov
 * Платежный провайдер
 */
@AllArgsConstructor
public enum PaymentGatewayProvider implements WithDescription {

    NONE_PAYMENT_PROVIDER("Отсутствует"),
    MOBI("Моби.Деньги"),
    REGISTRY("Платежные реестры (Банк)");

    @Getter
    private String description;
}
