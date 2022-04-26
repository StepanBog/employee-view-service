package tech.inno.odp.backend.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import tech.inno.odp.backend.data.containers.Tariff;
import tech.inno.odp.backend.mapper.common.*;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class,
        ProtoEnumMapper.class,
},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TariffMapper {

    tech.inno.odp.grpc.generated.service.Tariff transform(Tariff tariff);

    Tariff transform(tech.inno.odp.grpc.generated.service.Tariff tariff);

}
