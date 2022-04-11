package tech.inno.odp.backend.service.impl;

import com.vaadin.flow.data.provider.Query;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import tech.inno.odp.backend.data.containers.Employee;
import tech.inno.odp.backend.data.containers.Salary;
import tech.inno.odp.backend.mapper.EmployeeMapper;
import tech.inno.odp.backend.mapper.SalaryMapper;
import tech.inno.odp.backend.service.IEmployeeService;
import tech.inno.odp.backend.service.ISalaryService;
import tech.inno.odp.grpc.generated.service.employee.EmployeeServiceGrpc;
import tech.inno.odp.grpc.generated.service.employee.EmployeesResponse;
import tech.inno.odp.grpc.generated.service.employee.FindOneByIdEmployeeRequest;
import tech.inno.odp.grpc.generated.service.employee.SearchEmployeeRequest;
import tech.inno.odp.grpc.generated.service.salary.FindSalaryByEmployeeRequest;
import tech.inno.odp.grpc.generated.service.salary.FindSalaryByIdRequest;
import tech.inno.odp.grpc.generated.service.salary.SalaryResponse;
import tech.inno.odp.grpc.generated.service.salary.SalaryServiceGrpc;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author VKozlov
 */
@Service
@RequiredArgsConstructor
public class SalaryService implements ISalaryService {

    @GrpcClient("odp-service")
    private SalaryServiceGrpc.SalaryServiceBlockingStub salaryClient;

    private final SalaryMapper salaryMapper;

    @Override
    public List<Salary> findByEmployeeId(@NotNull UUID employeeId) {
        SalaryResponse response = salaryClient.findByEmployeeId(
                FindSalaryByEmployeeRequest.newBuilder()
                        .setEmployeeId(employeeId.toString())
                        .build()
        );
        
        return salaryMapper.transform(
                response.getSalariesList()
        );
    }

    @Override
    public Salary findById(@NotNull UUID id) {
        return salaryMapper.transform(
                salaryClient.findOneById(
                        FindSalaryByIdRequest.newBuilder()
                                .setId(id.toString())
                                .build()
                )
        );
    }

    @Override
    public Salary save(@NotNull Salary salary) {
        return salaryMapper.transform(
                salaryClient.save(
                        salaryMapper.transform(salary)
                )
        );
    }
}
