package ru.bogdanov.diplom.backend.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.bogdanov.diplom.backend.data.containers.Auth;
import ru.bogdanov.diplom.backend.mapper.common.BoolValueMapper;
import ru.bogdanov.diplom.backend.mapper.common.StringValueMapper;
import ru.bogdanov.diplom.backend.mapper.common.TimestampMapper;
import ru.bogdanov.diplom.backend.mapper.common.UUIDValueMapper;
import ru.bogdanov.diplom.grpc.generated.auth.AuthResponse;
import ru.bogdanov.diplom.grpc.generated.auth.token.TokenResponse;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class,
        UserMapper.class
},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AuthMapper {

    AuthResponse transform(Auth auth);

    Auth transform(AuthResponse response);

    Auth transform(TokenResponse response);

    default SimpleGrantedAuthority map(String role) {
        return new SimpleGrantedAuthority(role);
    }

}
