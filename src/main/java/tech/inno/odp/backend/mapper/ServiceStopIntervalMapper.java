package tech.inno.odp.backend.mapper;

import org.mapstruct.Mapper;
import tech.inno.odp.backend.data.containers.ServiceStopInterval;
import tech.inno.odp.backend.mapper.common.TimestampMapper;
import tech.inno.odp.backend.mapper.common.UUIDValueMapper;

import java.util.List;

@Mapper(uses = {TimestampMapper.class, UUIDValueMapper.class})
public interface ServiceStopIntervalMapper {

    ServiceStopInterval transform(tech.inno.odp.grpc.generated.service.ServiceStopInterval serviceStopInterval);

    tech.inno.odp.grpc.generated.service.ServiceStopInterval transform(ServiceStopInterval serviceStopInterval);

    List<ServiceStopInterval> transform(List<tech.inno.odp.grpc.generated.service.ServiceStopInterval> serviceStopIntervals);
}
