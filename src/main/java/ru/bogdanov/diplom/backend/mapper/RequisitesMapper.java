package ru.bogdanov.diplom.backend.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.bogdanov.diplom.backend.data.containers.Requisites;
import ru.bogdanov.diplom.backend.mapper.common.BoolValueMapper;
import ru.bogdanov.diplom.backend.mapper.common.StringValueMapper;
import ru.bogdanov.diplom.backend.mapper.common.TimestampMapper;
import ru.bogdanov.diplom.backend.mapper.common.UUIDValueMapper;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class,
},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface RequisitesMapper {

    ru.bogdanov.diplom.grpc.generated.Requisites transform(Requisites requisites);

    Requisites transform(ru.bogdanov.diplom.grpc.generated.Requisites requisites);

}
