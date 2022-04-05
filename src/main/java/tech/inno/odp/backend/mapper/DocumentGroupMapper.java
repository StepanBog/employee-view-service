package tech.inno.odp.backend.mapper;

import org.mapstruct.*;
import tech.inno.odp.backend.data.containers.document.DocumentTemplateGroup;
import tech.inno.odp.backend.mapper.common.BoolValueMapper;
import tech.inno.odp.backend.mapper.common.StringValueMapper;
import tech.inno.odp.backend.mapper.common.TimestampMapper;
import tech.inno.odp.backend.mapper.common.UUIDValueMapper;
import tech.inno.odp.backend.mapper.status.StatusMapper;
import tech.inno.odp.grpc.generated.documentsTemplate.DocumentTemplateGroupSearchRequest;

import java.util.List;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class,
        StatusMapper.class,
        DocumentTemplateMapper.class
},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DocumentGroupMapper {

    @Mapping(target = "templatesList", source = "templates")
    tech.inno.odp.grpc.generated.documents.DocumentTemplateGroup transform(DocumentTemplateGroup group);

    @Mapping(target = "templates", source = "templatesList")
    DocumentTemplateGroup transform(tech.inno.odp.grpc.generated.documents.DocumentTemplateGroup group);

    List<DocumentTemplateGroup> transform(List<tech.inno.odp.grpc.generated.documents.DocumentTemplateGroup> groups);

    @Mapping(target = "employerId", source = "group.employerId")
    @Mapping(target = "name", source = "group.name")
    @Mapping(target = "pageNumber", source = "pageNumber")
    @Mapping(target = "pageSize", source = "pageSize")
    DocumentTemplateGroupSearchRequest transformToSearch(DocumentTemplateGroup group,
                                                         int pageNumber,
                                                         int pageSize);

}
