package ru.bogdanov.diplom.backend.mapper;

import org.mapstruct.*;
import ru.bogdanov.diplom.backend.data.containers.Employer;
import ru.bogdanov.diplom.backend.mapper.common.*;
import ru.bogdanov.diplom.grpc.generated.service.employer.SearchEmployerRequest;

import java.util.List;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class,
        ProtoEnumMapper.class,
        RequisitesMapper.class,
},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface EmployerMapper {

    @Mapping(target = "requisites.inn", source = "inn")
    @Mapping(target = "requisites.kpp", source = "kpp")
    ru.bogdanov.diplom.grpc.generated.Employer transform(Employer employer);

    @Mapping(target = "inn", source = "employer.requisites.inn")
    @Mapping(target = "kpp", source = "employer.requisites.kpp")
    @Mapping(target = "contacts", source = "contactsList")
    Employer transform(ru.bogdanov.diplom.grpc.generated.Employer employer);

    List<Employer> transform(List<ru.bogdanov.diplom.grpc.generated.Employer> employers);

    @Mapping(target = "employerId", source = "employer.id")
    @Mapping(target = "pageNumber", source = "pageNumber")
    @Mapping(target = "pageSize", source = "pageSize")
    SearchEmployerRequest transformToSearch(Employer employer,
                                            int pageNumber,
                                            int pageSize);

}
