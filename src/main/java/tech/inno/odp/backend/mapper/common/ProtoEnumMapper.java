package tech.inno.odp.backend.mapper.common;

import org.mapstruct.Mapper;
import tech.inno.odp.backend.data.enums.*;

/**
 * @author VKozlov
 */
@Mapper
public interface ProtoEnumMapper {

    default tech.inno.odp.grpc.generated.common.employer.EmployerStatus mapToProto(EmployerStatus status) {
        return tech.inno.odp.grpc.generated.common.employer.EmployerStatus.valueOf(status.name());
    }

    default EmployerStatus mapTo(tech.inno.odp.grpc.generated.common.employer.EmployerStatus status) {
        return EmployerStatus.valueOf(status.name());
    }

    default tech.inno.odp.grpc.generated.common.CommissionPayer mapToProto(CommissionPayer commissionPayer) {
        return tech.inno.odp.grpc.generated.common.CommissionPayer.valueOf(commissionPayer.name());
    }

    default CommissionPayer mapTo(tech.inno.odp.grpc.generated.common.CommissionPayer commissionPayer) {
        return CommissionPayer.valueOf(commissionPayer.name());
    }

    default tech.inno.odp.grpc.generated.common.PaymentGatewayProvider mapToProto(
            PaymentGatewayProvider paymentProvider) {
        return tech.inno.odp.grpc.generated.common.PaymentGatewayProvider.valueOf(paymentProvider.name());
    }

    default PaymentGatewayProvider mapTo(tech.inno.odp.grpc.generated.common.PaymentGatewayProvider status) {
        return PaymentGatewayProvider.valueOf(status.name());
    }

    default tech.inno.odp.grpc.generated.common.transaction.TransactionStatus mapToProto(TransactionStatus status) {
        return tech.inno.odp.grpc.generated.common.transaction.TransactionStatus.valueOf(status.name());
    }

    default TransactionStatus mapTo(tech.inno.odp.grpc.generated.common.transaction.TransactionStatus status) {
        return TransactionStatus.valueOf(status.name());
    }

    default tech.inno.odp.grpc.generated.documents.DocumentGroupType mapToProto(DocumentGroupType type) {
        return tech.inno.odp.grpc.generated.documents.DocumentGroupType.valueOf(type.name());
    }

    default DocumentGroupType mapTo(tech.inno.odp.grpc.generated.documents.DocumentGroupType type) {
        return DocumentGroupType.valueOf(type.name());
    }

    default tech.inno.odp.grpc.generated.documents.DocumentType mapToProto(DocumentType type) {
        return tech.inno.odp.grpc.generated.documents.DocumentType.valueOf(type.name());
    }

    default DocumentType mapTo(tech.inno.odp.grpc.generated.documents.DocumentType type) {
        return DocumentType.valueOf(type.name());
    }

    default tech.inno.odp.grpc.generated.common.employee.EmployeeStatus mapToProto(EmployeeStatus status) {
        return tech.inno.odp.grpc.generated.common.employee.EmployeeStatus.valueOf(status.name());
    }

    default EmployeeStatus mapTo(tech.inno.odp.grpc.generated.common.employee.EmployeeStatus status) {
        return EmployeeStatus.valueOf(status.name());
    }

    default tech.inno.odp.grpc.generated.common.UserRole mapToProto(UserRoleName role) {
        return tech.inno.odp.grpc.generated.common.UserRole.valueOf(role.name());
    }

    default UserRoleName mapTo(tech.inno.odp.grpc.generated.common.UserRole role) {
        return UserRoleName.valueOf(role.name());
    }

    default tech.inno.odp.grpc.generated.service.TariffStatus mapToProto(TariffStatus tariffStatus) {
        return tech.inno.odp.grpc.generated.service.TariffStatus.valueOf(tariffStatus.name());
    }

    default TariffStatus mapTo(tech.inno.odp.grpc.generated.service.TariffStatus tariffStatus) {
        return TariffStatus.valueOf(tariffStatus.name());
    }

    default tech.inno.odp.grpc.generated.service.SpecTariffCondition mapToProto(
            SpecTariffCondition specTariffCondition) {
        return tech.inno.odp.grpc.generated.service.SpecTariffCondition.valueOf(specTariffCondition.name());
    }

    default SpecTariffCondition mapTo(tech.inno.odp.grpc.generated.service.SpecTariffCondition specTariffCondition) {
        return SpecTariffCondition.valueOf(specTariffCondition.name());
    }
}
