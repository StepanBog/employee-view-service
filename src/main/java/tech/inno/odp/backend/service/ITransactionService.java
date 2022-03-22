package tech.inno.odp.backend.service;

import tech.inno.odp.backend.data.containers.Transaction;
import tech.inno.odp.grpc.generated.service.transaction.SearchTransactionRequest;
import tech.inno.odp.grpc.generated.service.transaction.TransactionSearchResponse;

import javax.validation.constraints.NotNull;
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
     * Найти транзакцию по id
     *
     * @param transactionId - id транзакции
     * @return - транзакция
     */
    Transaction findById(final @NotNull UUID transactionId);
}
