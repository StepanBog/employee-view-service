package tech.inno.odp.backend.mapper;

import org.mapstruct.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tech.inno.odp.backend.data.containers.User;
import tech.inno.odp.backend.mapper.common.BoolValueMapper;
import tech.inno.odp.backend.mapper.common.StringValueMapper;
import tech.inno.odp.backend.mapper.common.TimestampMapper;
import tech.inno.odp.backend.mapper.common.UUIDValueMapper;
import tech.inno.odp.backend.mapper.status.StatusMapper;
import tech.inno.odp.grpc.generated.auth.user.UserSearchRequest;

import java.util.List;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class,
        UserSettingsMapper.class,
        StatusMapper.class,
},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

    tech.inno.odp.grpc.generated.auth.model.User transform(User user);

    @Mapping(target = "authorities", source = "rolesList")
    @Mapping(target = "roleNames", source = "rolesList")
    User transform(tech.inno.odp.grpc.generated.auth.model.User user);

    List<User> transform(List<tech.inno.odp.grpc.generated.auth.model.User> users);

    @Mapping(target = "employeeId", source = "user.employeeId")
    @Mapping(target = "employerId", source = "user.employerId")
    @Mapping(target = "login", source = "user.username")
    @Mapping(target = "rolesList", source = "user.roleNames")
    @Mapping(target = "pageNumber", source = "pageNumber")
    @Mapping(target = "pageSize", source = "pageSize")
    UserSearchRequest transformToSearch(User user,
                                        int pageNumber,
                                        int pageSize);

    default SimpleGrantedAuthority map(String role) {
        return new SimpleGrantedAuthority(role);
    }

}
