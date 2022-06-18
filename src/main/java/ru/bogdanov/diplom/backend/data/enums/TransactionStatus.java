package ru.bogdanov.diplom.backend.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author SBogdanov
 * Статус транзакции
 */
@AllArgsConstructor
public enum TransactionStatus implements WithDescription {

    AWAITING_CONFORMATION("Ожидает подтверждения"),
    CONFIRMED("Запрос подтвержден"),
    DECLINE("Запрос отклонен"),
    EXPIRED("Запрос просрочена"),
    WITHDRAWN("Запрос отозван");

    @Getter
    private String description;
}
