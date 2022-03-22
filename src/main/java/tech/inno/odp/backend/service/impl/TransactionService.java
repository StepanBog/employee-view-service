package tech.inno.odp.backend.service.impl;

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
    public Transaction findById(@NotNull UUID transactionID) {
        return transactionMapper.transform(
                transactionClient.findOneById(
                        FindByIdRequest.newBuilder()
                                .setTransactionId(transactionID.toString())
                                .build()
                )
        );
    }
}
