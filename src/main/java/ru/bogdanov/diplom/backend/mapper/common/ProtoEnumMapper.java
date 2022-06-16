package ru.bogdanov.diplom.backend.mapper.common;

import org.mapstruct.Mapper;
import ru.bogdanov.diplom.backend.data.enums.ContactPosition;
import ru.bogdanov.diplom.backend.data.enums.EmployeeStatus;
import ru.bogdanov.diplom.backend.data.enums.TransactionStatus;
import ru.bogdanov.diplom.backend.data.enums.UserRoleName;
import ru.bogdanov.diplom.grpc.generated.ContactsPosition;

/**
 * @author SBogdanov
 */
@Mapper
public interface ProtoEnumMapper {

    default ru.bogdanov.diplom.grpc.generated.TransactionStatus mapToProto(TransactionStatus status) {
        return ru.bogdanov.diplom.grpc.generated.TransactionStatus.valueOf(status.name());
    }

    default TransactionStatus mapTo(ru.bogdanov.diplom.grpc.generated.TransactionStatus status) {
        return TransactionStatus.valueOf(status.name());
    }

    default ru.bogdanov.diplom.grpc.generated.EmployeeStatus mapToProto(EmployeeStatus status) {
        return ru.bogdanov.diplom.grpc.generated.EmployeeStatus.valueOf(status.name());
    }

    default EmployeeStatus mapTo(ru.bogdanov.diplom.grpc.generated.EmployeeStatus status) {
        return EmployeeStatus.valueOf(status.name());
    }

    default ru.bogdanov.diplom.grpc.generated.auth.model.UserRole mapToProto(UserRoleName role) {
        return ru.bogdanov.diplom.grpc.generated.auth.model.UserRole.valueOf(role.name());
    }

    default UserRoleName mapTo(ru.bogdanov.diplom.grpc.generated.auth.model.UserRole role) {
        return UserRoleName.valueOf(role.name());
    }
    default ContactsPosition mapToProto(
            ContactPosition position) {
        return ContactsPosition.valueOf(position.name());
    }

    default ContactPosition mapTo(ContactsPosition position) {
        return ContactPosition.valueOf(position.name());
    }

}
