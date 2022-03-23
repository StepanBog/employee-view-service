package tech.inno.odp.backend.service;

import com.vaadin.flow.data.provider.Query;
import tech.inno.odp.backend.data.containers.Transaction;
import tech.inno.odp.grpc.generated.service.transaction.SearchTransactionRequest;
import tech.inno.odp.grpc.generated.service.transaction.TransactionSearchResponse;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author VKozlov
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
}
