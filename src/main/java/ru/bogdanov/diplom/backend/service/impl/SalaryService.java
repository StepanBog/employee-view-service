package ru.bogdanov.diplom.backend.service.impl;

import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.bogdanov.diplom.backend.data.containers.Salary;
import ru.bogdanov.diplom.backend.mapper.SalaryMapper;
import ru.bogdanov.diplom.backend.service.ISalaryService;
import ru.bogdanov.diplom.grpc.generated.service.salary.FindSalaryByEmployeeRequest;
import ru.bogdanov.diplom.grpc.generated.service.salary.FindSalaryByIdRequest;
import ru.bogdanov.diplom.grpc.generated.service.salary.SalaryResponse;
import ru.bogdanov.diplom.grpc.generated.service.salary.SalaryServiceGrpc;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author SBogdanov
 */
@Service
@RequiredArgsConstructor
public class SalaryService implements ISalaryService {

    @GrpcClient("main-service")
    private SalaryServiceGrpc.SalaryServiceBlockingStub salaryClient;

    private final SalaryMapper salaryMapper;

    @Override
    public Salary findByEmployeeId(@NotNull UUID employeeId) {
        SalaryResponse response = salaryClient.findByEmployeeId(
                FindSalaryByEmployeeRequest.newBuilder()
                        .setEmployeeId(employeeId.toString())
                        .build()
        );
        
        return salaryMapper.transform(
                response.getSalary()
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
