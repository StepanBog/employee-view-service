package tech.inno.odp.backend.service.impl;

import com.vaadin.flow.data.provider.Query;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import tech.inno.odp.backend.data.containers.Employee;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.mapper.EmployeeMapper;
import tech.inno.odp.backend.mapper.EmployerMapper;
import tech.inno.odp.backend.service.IEmployeeService;
import tech.inno.odp.grpc.generated.common.employee.EmployeeStatus;
import tech.inno.odp.grpc.generated.service.employee.CountEmployeesByEmployerRequest;
import tech.inno.odp.grpc.generated.service.employee.EmployeeServiceGrpc;
import tech.inno.odp.grpc.generated.service.employee.EmployeesResponse;
import tech.inno.odp.grpc.generated.service.employee.FindOneByIdEmployeeRequest;
import tech.inno.odp.grpc.generated.service.employee.SearchEmployeeRequest;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author VKozlov
 */
@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {

    @GrpcClient("odp-service")
    private EmployeeServiceGrpc.EmployeeServiceBlockingStub employeeClient;

    private final EmployeeMapper employeeMapper;
    private final EmployerMapper employerMapper;

    public EmployeesResponse find(final @NotNull SearchEmployeeRequest request) {
        return employeeClient.find(request);
    }

    @Override
    public List<Employee> find(Query<Employee, Employee> query, int pageSize) {
        SearchEmployeeRequest request = employeeMapper.transformToSearch(
                query.getFilter().orElse(null),
                query.getOffset() == 0 ? 0 : query.getOffset() / pageSize,
                pageSize
        );
        EmployeesResponse response = find(request);
        return employeeMapper.transform(response.getEmployeesList());
    }

    @Override
    public Employee findById(@NotNull UUID employerId) {
        return employeeMapper.transform(
                employeeClient.findOneById(
                        FindOneByIdEmployeeRequest.newBuilder()
                                .setId(employerId.toString())
                                .build()
                )
        );
    }

    @Override
    public Employee save(@NotNull Employee employee) {
        return employeeMapper.transform(
                employeeClient.update(
                        employeeMapper.transform(employee)
                )
        );
    }

    @Override
    public int getTotalCount(Query<Employee, Employee> query) {
        SearchEmployeeRequest request = employeeMapper.transformToSearch(
                query.getFilter().orElse(null),
                0,
                5
        );
        return (int) find(request).getTotalSize();
    }

    @Override
    public long countEmployeesByEmployer(@NotNull Employer employer, EmployeeStatus status) {
        CountEmployeesByEmployerRequest request = CountEmployeesByEmployerRequest.newBuilder()
                .setEmployer(employerMapper.transform(employer))
                .build();
        if (status != null) {
            request = request.toBuilder().setStatus(status).build();
        }
        return employeeClient.countEmployeesByEmployer(request).getCount();
    }

    @Override
    public long countEmployeesByEmployer(@NotNull Employer employer) {
        return countEmployeesByEmployer(employer, null);
    }
}
