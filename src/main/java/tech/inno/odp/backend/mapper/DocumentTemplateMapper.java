package tech.inno.odp.backend.mapper;

import org.mapstruct.*;
import tech.inno.odp.backend.data.containers.document.DocumentTemplate;
import tech.inno.odp.backend.mapper.common.BoolValueMapper;
import tech.inno.odp.backend.mapper.common.StringValueMapper;
import tech.inno.odp.backend.mapper.common.TimestampMapper;
import tech.inno.odp.backend.mapper.common.UUIDValueMapper;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class,
        DocumentBodyMapper.class,
},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DocumentTemplateMapper {


    tech.inno.odp.grpc.generated.documents.DocumentTemplate transform(DocumentTemplate documentTemplate);

    DocumentTemplate transform(tech.inno.odp.grpc.generated.documents.DocumentTemplate documentTemplate);

}
