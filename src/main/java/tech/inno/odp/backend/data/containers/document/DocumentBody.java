package tech.inno.odp.backend.data.containers.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentBody {

    private String id;

    /**
     * тип документа
     */
    private String contentType;

    /**
     * Расширение документа
     */
    private String extension;

    /**
     * Тело документа
     */
    private byte[] file;
}
