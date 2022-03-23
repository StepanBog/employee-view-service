package tech.inno.odp.backend.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import tech.inno.odp.backend.data.containers.Auth;
import tech.inno.odp.backend.mapper.common.BoolValueMapper;
import tech.inno.odp.backend.mapper.common.StringValueMapper;
import tech.inno.odp.backend.mapper.common.TimestampMapper;
import tech.inno.odp.backend.mapper.common.UUIDValueMapper;
import tech.inno.odp.grpc.generated.auth.AuthResponse;
import tech.inno.odp.grpc.generated.auth.token.TokenResponse;

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

    //TODO вернуть
//    default SimpleGrantedAuthority map(String role) {
//        return new SimpleGrantedAuthority(role);
//    }

}
