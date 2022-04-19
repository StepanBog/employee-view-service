package tech.inno.odp.backend.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import tech.inno.odp.backend.data.enums.UserRoleName;
import tech.inno.odp.backend.mapper.common.BoolValueMapper;
import tech.inno.odp.backend.mapper.common.StringValueMapper;
import tech.inno.odp.backend.mapper.common.TimestampMapper;
import tech.inno.odp.backend.mapper.common.UUIDValueMapper;
import tech.inno.odp.grpc.generated.auth.model.Role;
import tech.inno.odp.grpc.generated.common.UserRole;


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
        return tech.inno.odp.grpc.generated.auth.model.Role.newBuilder()
                        .setRoleName(UserRole.valueOf(roleName.name()))
                        .setDescription(roleName.getDescription())
                        .build();
    }
}
