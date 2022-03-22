package tech.inno.odp.backend.mapper;

import org.mapstruct.*;
import tech.inno.odp.backend.data.containers.Employee;
import tech.inno.odp.backend.mapper.common.BoolValueMapper;
import tech.inno.odp.backend.mapper.common.StringValueMapper;
import tech.inno.odp.backend.mapper.common.TimestampMapper;
import tech.inno.odp.backend.mapper.common.UUIDValueMapper;
import tech.inno.odp.backend.mapper.status.StatusMapper;
import tech.inno.odp.grpc.generated.service.employee.SearchEmployeeRequest;

import java.util.List;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class,
        StatusMapper.class,
        RequisitesMapper.class,
        PositionMapper.class,
        ServiceStopIntervalMapper.class
},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface EmployeeMapper {

    @Mapping(target = "employer.id", source = "employerId")
    tech.inno.odp.grpc.generated.service.Employee transform(Employee employee);

    @Mapping(target = "employerId", source = "employer.id")
    @Mapping(target = "firstName", source = "requisites.firstName")
    @Mapping(target = "lastName", source = "requisites.lastName")
    Employee transform(tech.inno.odp.grpc.generated.service.Employee employee);

    List<Employee> transform(List<tech.inno.odp.grpc.generated.service.Employee> employees);

    @Mapping(target = "employerId", source = "employer.employerId")
    @Mapping(target = "pageNumber", source = "pageNumber")
    @Mapping(target = "pageSize", source = "pageSize")
    SearchEmployeeRequest transformToSearch(Employee employer,
                                            int pageNumber,
                                            int pageSize);

}
