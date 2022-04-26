package tech.inno.odp.backend.mapper;

import org.mapstruct.*;
import tech.inno.odp.backend.data.containers.Transaction;
import tech.inno.odp.backend.mapper.common.BoolValueMapper;
import tech.inno.odp.backend.mapper.common.StringValueMapper;
import tech.inno.odp.backend.mapper.common.TimestampMapper;
import tech.inno.odp.backend.mapper.common.UUIDValueMapper;
import tech.inno.odp.backend.mapper.common.ProtoEnumMapper;
import tech.inno.odp.grpc.generated.service.transaction.SearchTransactionRequest;

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

    tech.inno.odp.grpc.generated.service.Transaction transform(Transaction transaction);

    Transaction transform(tech.inno.odp.grpc.generated.service.Transaction transaction);

    List<Transaction> transform(List<tech.inno.odp.grpc.generated.service.Transaction> transactions);

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
