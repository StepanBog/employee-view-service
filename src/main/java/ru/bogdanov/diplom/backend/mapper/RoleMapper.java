package ru.bogdanov.diplom.backend.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.bogdanov.diplom.backend.data.enums.UserRoleName;
import ru.bogdanov.diplom.backend.mapper.common.BoolValueMapper;
import ru.bogdanov.diplom.backend.mapper.common.StringValueMapper;
import ru.bogdanov.diplom.backend.mapper.common.TimestampMapper;
import ru.bogdanov.diplom.backend.mapper.common.UUIDValueMapper;
import ru.bogdanov.diplom.grpc.generated.auth.model.Role;
import ru.bogdanov.diplom.grpc.generated.auth.model.UserRole;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {

    default UserRoleName transform(Role role) {
        return UserRoleName.valueOf(role.getRoleName().name());
    }

    default Role transform(UserRoleName roleName) {
        return Role.newBuilder()
                        .setRoleName(UserRole.valueOf(roleName.name()))
                        .setDescription(roleName.getDescription())
                        .build();
    }
}
