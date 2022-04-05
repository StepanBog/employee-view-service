package tech.inno.odp.backend.data.containers.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentTemplateGroup {

    private String id;

    /**
     * Имя шаблона
     */
    @NotEmpty(message = "Имя не может быть пустым")
    private String name;

    /**
     * Флаг указывающий актуальную используемую группу группу
     */
    private boolean actualGroup;

    /**
     * Идентификатор работодателя
     */
    private String employerId;

    /**
     * Версия набора документов
     */
    private Integer version;

    /**
     * Шаблоны документов в наборе
     */
    @NotNull(message = "Набор шаблонов должен иметь хотя бы один шаблон")
    @Size(min = 1, message = "Набор шаблонов должен иметь хотя бы один шаблон")
    private List<DocumentTemplate> templates;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
