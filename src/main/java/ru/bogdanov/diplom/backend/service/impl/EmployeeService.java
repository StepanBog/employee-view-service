package ru.bogdanov.diplom.backend.service.impl;

import com.vaadin.flow.data.provider.Query;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.bogdanov.diplom.backend.data.containers.Employee;
import ru.bogdanov.diplom.backend.data.containers.Employer;
import ru.bogdanov.diplom.backend.mapper.EmployeeMapper;
import ru.bogdanov.diplom.backend.mapper.EmployerMapper;
import ru.bogdanov.diplom.backend.service.IEmployeeService;
import ru.bogdanov.diplom.grpc.generated.EmployeeStatus;
import ru.bogdanov.diplom.grpc.generated.service.employee.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author SBogdanov
 */
@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {

    @GrpcClient("main-service")
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
