package tech.inno.odp.backend.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author VKozlov
 */
@AllArgsConstructor
public enum DocumentType implements WithDescription {

    SES("Соглашение об использовании ПЭП"),
    ODP_TERMS("Условия предоставления сервиса ODP"),
    LNA("ЛНА"),
    APPLICATION_FOR_PAYMENT("Заявление на удержание и выплату аванса"),
    PAYMENT_CREATED_DOCUMENT("Документ формируемый при создании выплаты");

    @Getter
    private String description;
}
