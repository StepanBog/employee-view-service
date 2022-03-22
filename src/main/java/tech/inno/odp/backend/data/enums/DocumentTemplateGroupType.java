package tech.inno.odp.backend.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author VKozlov
 */
@AllArgsConstructor
public enum DocumentTemplateGroupType implements WithDescription {

    REGISTRATION_TEMPLATE_GROUP_TYPE("Документы для регистрации"),
    PAYMENT_CREATED_DOCUMENT_TEMPLATE_GROUP_TYPE("Документы при создании выплаты");

    @Getter
    private String description;
}
