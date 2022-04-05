package tech.inno.odp.backend.data.containers.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.inno.odp.backend.data.enums.DocumentGroupType;
import tech.inno.odp.grpc.generated.documents.DocumentType;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Document {

    private String id;

    /**
     * версия документа
     */
    private String version;

    /**
     * Идентификатор работника
     */
    private String employeeId;

    /**
     * Идентификатор работодателя
     */
    private String employerId;

    /**
     * Флаг, указывающий подписан ли документ
     */
    private Boolean signed;

    /**
     * Тело документа
     */
    @NotNull(message = "Шаблон должен иметь прикрепленный файл")
    private DocumentBody attachment;

    /**
     * Тип набора документов
     */
    @NotNull(message = "Документ должен иметь тип")
    private DocumentType type;

    /**
     * Тип набора документов
     */
    @NotNull(message = "Набор шаблонов должен иметь тип")
    private DocumentGroupType groupType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
