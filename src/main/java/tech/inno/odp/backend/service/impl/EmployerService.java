package tech.inno.odp.backend.service.impl;

import com.vaadin.flow.data.provider.Query;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.backend.mapper.EmployerMapper;
import tech.inno.odp.backend.service.IEmployerService;
import tech.inno.odp.grpc.generated.service.employer.EmployerServiceGrpc;
import tech.inno.odp.grpc.generated.service.employer.EmployersResponse;
import tech.inno.odp.grpc.generated.service.employer.OneEmployerRequest;
import tech.inno.odp.grpc.generated.service.employer.SearchEmployerRequest;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author VKozlov
 */
@Service
@RequiredArgsConstructor
public class EmployerService implements IEmployerService {

    @GrpcClient("odp-service")
    private EmployerServiceGrpc.EmployerServiceBlockingStub employerClient;

    private final EmployerMapper employerMapper;

    public EmployersResponse find(final @NotNull SearchEmployerRequest request) {
        return employerClient.find(request);
    }

    @Override
    public List<Employer> findAll(@NotNull SearchEmployerRequest request) {
        return employerMapper.transform(find(request).getEmployersList());
    }

    @Override
    public List<Employer> find(Query<Employer, Employer> query, int pageSize) {
        SearchEmployerRequest request = employerMapper.transformToSearch(
                query.getFilter().orElse(null),
                query.getOffset() == 0 ? 0 : query.getOffset() / pageSize,
                pageSize
        );
        EmployersResponse response = find(request);
        return employerMapper.transform(response.getEmployersList());
    }

    @Override
    public Employer findById(@NotNull UUID employerId) {
        return employerMapper.transform(
                employerClient.findOne(
                        OneEmployerRequest.newBuilder()
                                .setEmployerId(employerId.toString())
                                .build()
                )
        );
    }

    @Override
    @Transactional
    public Employer save(@NotNull Employer employer) {
        return employerMapper.transform(
                employerClient.save(
                        employerMapper.transform(employer)
                )
        );
    }

    @Override
    public int getTotalCount(Query<Employer, Employer> query) {
        SearchEmployerRequest request = employerMapper.transformToSearch(
                query.getFilter().orElse(null),
                0,
                5
        );
        return (int) find(request).getTotalSize();
    }
}
