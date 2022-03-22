package tech.inno.odp.backend.data.containers.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentTemplate {

    private String id;

    /**
     * Имя шаблона
     */
    @NotEmpty(message = "Имя не может быть пустым")
    private String name;

    /**
     * Тело документа
     */
    @NotNull(message = "Шаблон должен иметь прикрепленный файл")
    private DocumentBody attachment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
