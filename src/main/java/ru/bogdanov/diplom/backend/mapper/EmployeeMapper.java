package ru.bogdanov.diplom.backend.mapper;

import org.mapstruct.*;
import ru.bogdanov.diplom.backend.data.containers.Employee;
import ru.bogdanov.diplom.backend.mapper.common.*;
import ru.bogdanov.diplom.grpc.generated.service.employee.SearchEmployeeRequest;
import ru.bogdanov.diplom.utils.PhoneUtils;

import java.util.List;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class,
        ProtoEnumMapper.class,
        RequisitesMapper.class
},imports = {PhoneUtils.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface EmployeeMapper {

    @Mapping(target = "employer.id", source = "employerId")
    ru.bogdanov.diplom.grpc.generated.Employee transform(Employee employee);

    @Mapping(target = "employerId", source = "employer.id")
    @Mapping(target = "employerName", source = "employer.name")
    @Mapping(target = "firstName", source = "requisites.firstName")
    @Mapping(target = "lastName", source = "requisites.lastName")
    @Mapping(target = "patronymicName", source = "requisites.patronymicName")
    Employee transform( ru.bogdanov.diplom.grpc.generated.Employee employee);

    List<Employee> transform(List< ru.bogdanov.diplom.grpc.generated.Employee> employees);

    @Mapping(target = "employerId", source = "employee.employerId")
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "pageNumber", source = "pageNumber")
    @Mapping(target = "pageSize", source = "pageSize")
    @Mapping(target = "firstName", source = "employee.firstName")
    @Mapping(target = "lastName", source = "employee.lastName")
    @Mapping(target = "patronymicName", source = "employee.patronymicName")
    @Mapping(target = "phone",expression = "java(PhoneUtils.addPrefix(employee.getPhone()))")
    SearchEmployeeRequest transformToSearch(Employee employee,
                                            int pageNumber,
                                            int pageSize);

}
