package tech.inno.odp.backend.mapper;

import org.mapstruct.*;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.mapper.common.BoolValueMapper;
import tech.inno.odp.backend.mapper.common.StringValueMapper;
import tech.inno.odp.backend.mapper.common.TimestampMapper;
import tech.inno.odp.backend.mapper.common.UUIDValueMapper;
import tech.inno.odp.backend.mapper.status.StatusMapper;
import tech.inno.odp.grpc.generated.service.employer.SearchEmployerRequest;

import java.util.List;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class,
        StatusMapper.class,
        RequisitesMapper.class,
        TariffMapper.class
},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface EmployerMapper {

    @Mapping(target = "paymentProvider", source = "paymentProvider")
    tech.inno.odp.grpc.generated.service.Employer transform(Employer employer);

    @Mapping(target = "paymentProvider", source = "paymentProvider")
    Employer transform(tech.inno.odp.grpc.generated.service.Employer employer);

    List<Employer> transform(List<tech.inno.odp.grpc.generated.service.Employer> employers);

    @Mapping(target = "employerId", source = "employer.id")
    @Mapping(target = "pageNumber", source = "pageNumber")
    @Mapping(target = "pageSize", source = "pageSize")
    SearchEmployerRequest transformToSearch(Employer employer,
                                            int pageNumber,
                                            int pageSize);

}
