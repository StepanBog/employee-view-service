package tech.inno.odp.backend.mapper;

import org.mapstruct.*;
import tech.inno.odp.backend.data.containers.document.Document;
import tech.inno.odp.backend.mapper.common.BoolValueMapper;
import tech.inno.odp.backend.mapper.common.StringValueMapper;
import tech.inno.odp.backend.mapper.common.TimestampMapper;
import tech.inno.odp.backend.mapper.common.UUIDValueMapper;
import tech.inno.odp.backend.mapper.status.StatusMapper;
import tech.inno.odp.grpc.generated.documents.DocumentSearchRequest;

import java.util.List;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class,
        StatusMapper.class,
        DocumentBodyMapper.class,
},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DocumentMapper {

    tech.inno.odp.grpc.generated.documents.Document transform(Document document);

    Document transform(tech.inno.odp.grpc.generated.documents.Document Document);

    List<Document> transform(List<tech.inno.odp.grpc.generated.documents.Document> documents);

    @Mapping(target = "employeeId", source = "document.employeeId")
    @Mapping(target = "groupType", source = "document.type")
    @Mapping(target = "pageNumber", source = "pageNumber")
    @Mapping(target = "pageSize", source = "pageSize")
    DocumentSearchRequest transformToSearch(Document document,
                                            int pageNumber,
                                            int pageSize);

}
