package tech.inno.odp.backend.mapper.status;

import org.mapstruct.Mapper;
import tech.inno.odp.backend.data.enums.*;

/**
 * @author VKozlov
 */
@Mapper
public interface StatusMapper {

    default tech.inno.odp.grpc.generated.common.employer.EmployerStatus mapToProto(
            EmployerStatus status) {
        return tech.inno.odp.grpc.generated.common.employer.EmployerStatus.valueOf(status.name());
    }

    default EmployerStatus mapTo(tech.inno.odp.grpc.generated.common.employer.EmployerStatus status) {
        return EmployerStatus.valueOf(status.name());
    }

    default tech.inno.odp.grpc.generated.common.CommissionPayer mapToProto(
            CommissionPayer status) {
        return tech.inno.odp.grpc.generated.common.CommissionPayer.valueOf(status.name());
    }

    default CommissionPayer mapTo(tech.inno.odp.grpc.generated.common.CommissionPayer status) {
        return CommissionPayer.valueOf(status.name());
    }

    default tech.inno.odp.grpc.generated.integration.PaymentGatewayProvider mapToProto(
            PaymentGatewayProvider status) {
        return tech.inno.odp.grpc.generated.integration.PaymentGatewayProvider.valueOf(status.name());
    }

    default PaymentGatewayProvider mapTo(tech.inno.odp.grpc.generated.integration.PaymentGatewayProvider status) {
        return PaymentGatewayProvider.valueOf(status.name());
    }

    default tech.inno.odp.grpc.generated.common.transaction.TransactionStatus mapToProto(
            TransactionStatus status) {
        return tech.inno.odp.grpc.generated.common.transaction.TransactionStatus.valueOf(status.name());
    }

    default TransactionStatus mapTo(tech.inno.odp.grpc.generated.common.transaction.TransactionStatus status) {
        return TransactionStatus.valueOf(status.name());
    }

    default tech.inno.odp.grpc.generated.documents.DocumentTemplateGroupType mapToProto(DocumentTemplateGroupType type) {
        return tech.inno.odp.grpc.generated.documents.DocumentTemplateGroupType.valueOf(type.name());
    }

    default DocumentTemplateGroupType mapTo(tech.inno.odp.grpc.generated.documents.DocumentTemplateGroupType type) {
        return DocumentTemplateGroupType.valueOf(type.name());
    }

    default tech.inno.odp.grpc.generated.common.employee.EmployeeStatus mapToProto(EmployeeStatus status) {
        return tech.inno.odp.grpc.generated.common.employee.EmployeeStatus.valueOf(status.name());
    }

    default EmployeeStatus mapTo(tech.inno.odp.grpc.generated.common.employee.EmployeeStatus status) {
        return EmployeeStatus.valueOf(status.name());
    }
}
