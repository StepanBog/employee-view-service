package tech.inno.odp.backend.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author VKozlov
 */
@AllArgsConstructor
public enum DocumentGroupType implements WithDescription {

    REGISTRATION_GROUP_TYPE("Документы для регистрации"),
    PAYMENT_CREATED_DOCUMENT_GROUP_TYPE("Документы при создании выплаты");

    @Getter
    private String description;
}
