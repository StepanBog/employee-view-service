package tech.inno.odp.backend.mapper;

import org.mapstruct.*;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.mapper.common.BoolValueMapper;
import tech.inno.odp.backend.mapper.common.StringValueMapper;
import tech.inno.odp.backend.mapper.common.TimestampMapper;
import tech.inno.odp.backend.mapper.common.UUIDValueMapper;
import tech.inno.odp.backend.mapper.common.ProtoEnumMapper;
import tech.inno.odp.grpc.generated.service.employer.SearchEmployerRequest;

import java.util.List;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class,
        ProtoEnumMapper.class,
        RequisitesMapper.class,
        TariffMapper.class
},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface EmployerMapper {

    @Mapping(target = "paymentProvider", source = "paymentProvider")
    @Mapping(target = "tariffsList", source = "tariffs")
    @Mapping(target = "contactsList", source = "contacts")
    @Mapping(target = "requisites.inn", source = "inn")
    @Mapping(target = "requisites.kpp", source = "kpp")
    tech.inno.odp.grpc.generated.service.Employer transform(Employer employer);

    @Mapping(target = "paymentProvider", source = "paymentProvider")
    @Mapping(target = "tariffs", source = "tariffsList")
    @Mapping(target = "contacts", source = "contactsList")
    @Mapping(target = "inn", source = "employer.requisites.inn")
    @Mapping(target = "kpp", source = "employer.requisites.kpp")
    Employer transform(tech.inno.odp.grpc.generated.service.Employer employer);

    List<Employer> transform(List<tech.inno.odp.grpc.generated.service.Employer> employers);

    @Mapping(target = "employerId", source = "employer.id")
    @Mapping(target = "pageNumber", source = "pageNumber")
    @Mapping(target = "pageSize", source = "pageSize")
    SearchEmployerRequest transformToSearch(Employer employer,
                                            int pageNumber,
                                            int pageSize);

}
