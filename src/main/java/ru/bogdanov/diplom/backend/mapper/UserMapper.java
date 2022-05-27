package ru.bogdanov.diplom.backend.mapper;

import org.mapstruct.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.bogdanov.diplom.backend.data.containers.User;
import ru.bogdanov.diplom.backend.mapper.common.*;
import ru.bogdanov.diplom.grpc.generated.auth.model.Role;
import ru.bogdanov.diplom.grpc.generated.auth.user.UserSearchRequest;

import java.util.Collections;
import java.util.List;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class,
        ProtoEnumMapper.class,
        RoleMapper.class,
},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

    @Mapping(target = "role", source = "roleName")
    @Mapping(target = "enable", source = "enabled")
    ru.bogdanov.diplom.grpc.generated.auth.model.User transform(User user);

    @Mapping(target = "authorities", source = "role")
    @Mapping(target = "roleName", source = "role")
    @Mapping(target = "enabled", source = "enable")
    User transform( ru.bogdanov.diplom.grpc.generated.auth.model.User user);

    List<User> transform(List< ru.bogdanov.diplom.grpc.generated.auth.model.User> users);

    @Mapping(target = "employeeId", source = "user.employeeId")
    @Mapping(target = "employerId", source = "user.employerId")
    @Mapping(target = "login", source = "user.username")
    @Mapping(target = "role", source = "user.roleName")
    @Mapping(target = "pageNumber", source = "pageNumber")
    @Mapping(target = "pageSize", source = "pageSize")
    UserSearchRequest transformToSearch(User user,
                                        int pageNumber,
                                        int pageSize);

    default List<SimpleGrantedAuthority> map(Role role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getRoleName().name()));
    }
}
