package tech.inno.odp.backend.service.impl;

import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import tech.inno.odp.backend.data.containers.ServiceStopInterval;
import tech.inno.odp.backend.mapper.ServiceStopIntervalMapper;
import tech.inno.odp.backend.service.IServiceStopIntervalService;
import tech.inno.odp.grpc.generated.service.stop_interval.FindOneByEmployeeIdRequest;
import tech.inno.odp.grpc.generated.service.stop_interval.ServiceStopIntervalServiceGrpc;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author VKozlov
 */
@Service
@RequiredArgsConstructor
public class ServiceStopIntervalService implements IServiceStopIntervalService {

    @GrpcClient("odp-service")
    private ServiceStopIntervalServiceGrpc.ServiceStopIntervalServiceBlockingStub client;

    private final ServiceStopIntervalMapper mapper;

    @Override
    public List<ServiceStopInterval> findByEmployeeId(@NotNull UUID employeeId) {
        return mapper.transform(client.findByEmployeeId(
                FindOneByEmployeeIdRequest.newBuilder()
                        .setEmployeeId(employeeId.toString())
                        .build()
        ).getIntervalsList());
    }

    @Override
    public ServiceStopInterval save(@NotNull ServiceStopInterval serviceStopInterval) {
        return mapper.transform(
                client.save(
                        mapper.transform(serviceStopInterval)
                )
        );
    }
}
