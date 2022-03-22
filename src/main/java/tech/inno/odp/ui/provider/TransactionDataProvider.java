package tech.inno.odp.ui.provider;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.RequiredArgsConstructor;
import tech.inno.odp.backend.data.containers.Transaction;
import tech.inno.odp.backend.mapper.TransactionMapper;
import tech.inno.odp.backend.service.ITransactionService;
import tech.inno.odp.grpc.generated.service.transaction.SearchTransactionRequest;
import tech.inno.odp.grpc.generated.service.transaction.TransactionSearchResponse;

import java.util.stream.Stream;

/**
 * @author VKozlov
 */
@SpringComponent
@RequiredArgsConstructor
public class TransactionDataProvider extends AbstractBackEndDataProvider<Transaction, Transaction> {

    private final ITransactionService transactionService;
    private final TransactionMapper transactionMapper;

    private int totalSize = -1;

    @Override
    protected Stream<Transaction> fetchFromBackEnd(Query<Transaction, Transaction> query) {
        SearchTransactionRequest request = SearchTransactionRequest.newBuilder()
                .setPageNumber(query.getOffset())
                .setPageSize(query.getLimit())
                .build();
        TransactionSearchResponse response = transactionService.find(request);

        this.totalSize = (int) response.getTotalSize();
        return transactionMapper.transform(response.getTransactionsList()).stream();
    }

    @Override
    protected int sizeInBackEnd(Query<Transaction, Transaction> query) {
        if (this.totalSize < 0) {
            this.totalSize = getTransactionsCount();
        }
        return this.totalSize;
    }

    private int getTransactionsCount() {
        SearchTransactionRequest request = SearchTransactionRequest.newBuilder()
                .setPageNumber(0)
                .setPageSize(1)
                .build();
        return (int) transactionService.find(request).getTotalSize();
    }
}
