package ru.bogdanov.diplom.backend.mapper;

import org.mapstruct.*;
import ru.bogdanov.diplom.backend.data.containers.Transaction;
import ru.bogdanov.diplom.backend.mapper.common.*;
import ru.bogdanov.diplom.grpc.generated.service.transaction.SearchTransactionRequest;

import java.util.List;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class,
        ProtoEnumMapper.class
},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TransactionMapper {

    ru.bogdanov.diplom.grpc.generated.Transaction transform(Transaction transaction);

    Transaction transform(ru.bogdanov.diplom.grpc.generated.Transaction transaction);

    List<Transaction> transform(List<ru.bogdanov.diplom.grpc.generated.Transaction> transactions);

    @Mapping(target = "pageNumber", source = "pageNumber")
    @Mapping(target = "pageSize", source = "pageSize")
    @Mapping(target = "sum", source = "transaction.totalSum")
    @Mapping(target = "employerId", source = "transaction.employerId")
    @Mapping(target = "employeeId", source = "transaction.employeeId")
    @Mapping(target = "id", source = "transaction.id")
    SearchTransactionRequest transformToSearch(Transaction transaction,
                                               int pageNumber,
                                               int pageSize);

}
