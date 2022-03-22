package tech.inno.odp.backend.mapper;

import org.mapstruct.*;
import tech.inno.odp.backend.data.containers.document.DocumentBody;
import tech.inno.odp.backend.mapper.common.BoolValueMapper;
import tech.inno.odp.backend.mapper.common.StringValueMapper;
import tech.inno.odp.backend.mapper.common.TimestampMapper;
import tech.inno.odp.backend.mapper.common.UUIDValueMapper;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
@DecoratedWith(CustomDocumentBodyMapper.class)
public interface DocumentBodyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "file", ignore = true)
    DocumentBody transform(tech.inno.odp.grpc.generated.documents.DocumentBody documentBody);

    @Mapping(target = "file", ignore = true)
    tech.inno.odp.grpc.generated.documents.DocumentBody transform(DocumentBody attachment);
}
