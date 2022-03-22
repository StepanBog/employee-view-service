package tech.inno.odp.backend.service;

import com.vaadin.flow.data.provider.Query;
import tech.inno.odp.backend.data.containers.document.Document;
import tech.inno.odp.backend.data.containers.document.DocumentBody;
import tech.inno.odp.backend.data.containers.document.DocumentTemplateGroup;
import tech.inno.odp.grpc.generated.documents.DocumentSearchRequest;
import tech.inno.odp.grpc.generated.documents.DocumentsResponse;
import tech.inno.odp.grpc.generated.documentsTemplate.DocumentTemplateGroupSearchRequest;
import tech.inno.odp.grpc.generated.documentsTemplate.DocumentTemplateGroupsResponse;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author VKozlov
 * Сервис для работы с документами
 */
public interface IDocumentService {

    /**
     * Получить документы по критериям поиска
     *
     * @param request критерии поиска
     * @return объект со списком документов
     */
    DocumentsResponse find(final @NotNull DocumentSearchRequest request);

    /**
     * Найти документы
     *
     * @param query    - критерии поиска
     * @param pageSize - количество элементов выводимых на 1й странице
     * @return - список документов
     */
    List<Document> find(Query<Document, Document> query, int pageSize);

    /**
     * Получить файл шаблона по id шаблона
     *
     * @param documentId
     * @return сущность с инфомрацией о файле
     */
    DocumentBody findDocumentFile(UUID documentId);
    
    /**
     * Получить общее количество элементов
     *
     * @return общее количество
     */
    int getTotalCount(Query<Document, Document> query);
}
