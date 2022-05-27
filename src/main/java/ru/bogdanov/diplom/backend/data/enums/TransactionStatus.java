package ru.bogdanov.diplom.backend.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author SBogdanov
 * Статус транзакции
 */
@AllArgsConstructor
public enum TransactionStatus implements WithDescription {

    AWAITING_CONFIRMATION("Ожидаем подтверждения"),
    CONFIRMED("Платеж подтвержден"),
    PROCESSING("Платеж в обработке"),
    SUCCESS("Платеж успешно проведен платежным провайдером"),
    DECLINE("Платеж отклонен платежным провайдером"),
    TRANSACTION_ERROR("Ошибка платежа"),
    EXPIRED("Транзакция просрочена"),
    AWAITING_DISPATCH("Ожидает отправки в платежный сервис"),
    NEW("Новая транзакция");

    @Getter
    private String description;
}
