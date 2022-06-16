package ru.bogdanov.diplom.backend.service;

import com.vaadin.flow.data.provider.Query;
import ru.bogdanov.diplom.backend.data.containers.Transaction;
import ru.bogdanov.diplom.grpc.generated.service.transaction.SearchTransactionRequest;
import ru.bogdanov.diplom.grpc.generated.service.transaction.TransactionSearchResponse;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author SBogdanov
 * Сервис для работы с транзакциями
 */
public interface ITransactionService {

    /**
     * Найти транзакции
     *
     * @param request - критерии поиска транзакций
     * @return - список транзакций
     */
    TransactionSearchResponse find(final @NotNull SearchTransactionRequest request);

    /**
     * Найти транзакции
     *
     * @param query    - критерии поиска
     * @param pageSize - количество элементов выводимых на 1й странице
     * @return - список транзакций
     */
    List<Transaction> find(Query<Transaction, Transaction> query, int pageSize);

    /**
     * Найти транзакцию по id
     *
     * @param transactionId - id транзакции
     * @return - транзакция
     */
    Transaction findById(final @NotNull UUID transactionId);

    /**
     * Получить общее количество элементов
     *
     * @return общее количество
     */
    int getTotalCount(Query<Transaction, Transaction> query);

    /**
     * Создать запрос выплаты
     *
     * @return
     */
    Transaction pay(final @NotNull UUID employeeId,final long sum);

    /**
     * Обновить запрос выплаты
     */
    void update(final @NotNull Transaction transaction);
}
