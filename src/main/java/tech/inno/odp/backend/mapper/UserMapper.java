package tech.inno.odp.backend.mapper;

import org.mapstruct.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tech.inno.odp.backend.data.containers.User;
import tech.inno.odp.backend.mapper.common.BoolValueMapper;
import tech.inno.odp.backend.mapper.common.StringValueMapper;
import tech.inno.odp.backend.mapper.common.TimestampMapper;
import tech.inno.odp.backend.mapper.common.UUIDValueMapper;
import tech.inno.odp.backend.mapper.common.ProtoEnumMapper;
import tech.inno.odp.grpc.generated.auth.model.Role;
import tech.inno.odp.grpc.generated.auth.user.UserSearchRequest;

import java.util.List;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class,
        UserSettingsMapper.class,
        ProtoEnumMapper.class,
        RoleMapper.class,
},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

    @Mapping(target = "rolesList", source = "roleNames")
    @Mapping(target = "enable", source = "enabled")
    tech.inno.odp.grpc.generated.auth.model.User transform(User user);

    @Mapping(target = "authorities", source = "rolesList")
    @Mapping(target = "roleNames", source = "rolesList")
    @Mapping(target = "enabled", source = "enable")
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

    default SimpleGrantedAuthority map(Role role) {
        return new SimpleGrantedAuthority(role.getRoleName().name());
    }
}
