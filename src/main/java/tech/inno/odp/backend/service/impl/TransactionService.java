package tech.inno.odp.backend.service.impl;

import com.vaadin.flow.data.provider.Query;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import tech.inno.odp.backend.data.containers.Transaction;
import tech.inno.odp.backend.mapper.TransactionMapper;
import tech.inno.odp.backend.service.ITransactionService;
import tech.inno.odp.grpc.generated.service.transaction.FindByIdRequest;
import tech.inno.odp.grpc.generated.service.transaction.SearchTransactionRequest;
import tech.inno.odp.grpc.generated.service.transaction.TransactionSearchResponse;
import tech.inno.odp.grpc.generated.service.transaction.TransactionServiceGrpc;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author VKozlov
 */
@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    @GrpcClient("odp-service")
    private TransactionServiceGrpc.TransactionServiceBlockingStub transactionClient;

    private final TransactionMapper transactionMapper;

    public TransactionSearchResponse find(final @NotNull SearchTransactionRequest request) {
        return transactionClient.find(request);
    }

    @Override
    public List<Transaction> find(Query<Transaction, Transaction> query, int pageSize) {
        SearchTransactionRequest request = transactionMapper.transformToSearch(
                query.getFilter().orElse(null),
                query.getOffset() == 0 ? 0 : query.getOffset() / pageSize,
                pageSize
        );
        TransactionSearchResponse response = find(request);
        return transactionMapper.transform(response.getTransactionsList());
    }

    @Override
    public Transaction findById(@NotNull UUID transactionID) {
        return transactionMapper.transform(
                transactionClient.findOneById(
                        FindByIdRequest.newBuilder()
                                .setTransactionId(transactionID.toString())
                                .build()
                )
        );
    }

    @Override
    public int getTotalCount(Query<Transaction, Transaction> query) {
        SearchTransactionRequest request = transactionMapper.transformToSearch(
                query.getFilter().orElse(null),
                0,
                5
        );
        return (int) find(request).getTotalSize();
    }
}
