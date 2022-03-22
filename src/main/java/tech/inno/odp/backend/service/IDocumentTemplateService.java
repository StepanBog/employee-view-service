package tech.inno.odp.backend.service;

import com.vaadin.flow.data.provider.Query;
import tech.inno.odp.backend.data.containers.document.DocumentBody;
import tech.inno.odp.backend.data.containers.document.DocumentTemplateGroup;
import tech.inno.odp.grpc.generated.documentsTemplate.DocumentTemplateGroupSearchRequest;
import tech.inno.odp.grpc.generated.documentsTemplate.DocumentTemplateGroupsResponse;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author VKozlov
 * Сервис для работы с сервисом шаблонов документов
 */
public interface IDocumentTemplateService {

    /**
     * Получить шаблоны документов по критериям поиска
     *
     * @param request критерии поиска
     * @return объект со списком шаблонов документов
     */
    DocumentTemplateGroupsResponse find(final @NotNull DocumentTemplateGroupSearchRequest request);

    /**
     * Найти наблоны документов
     *
     * @param query    - критерии поиска
     * @param pageSize - количество элементов выводимых на 1й странице
     * @return - список шаблоны документов
     */
    List<DocumentTemplateGroup> find(Query<DocumentTemplateGroup, DocumentTemplateGroup> query, int pageSize);

    /**
     * Найти набор документов по id
     *
     * @param id - id набора документов
     * @return - набор документов найденный по id
     */
    DocumentTemplateGroup findById(final @NotNull UUID id);

    /**
     * Получить файл шаблона по id шаблона
     *
     * @param documentTemplateId
     * @return сущность с инфомрацией о файле
     */
    DocumentBody findDocumentFile(UUID documentTemplateId);

    /**
     * Сохранить группу шаблонов документов
     *
     * @param documentTemplateGroup - сущность для сохранения
     */
    void save(final @NotNull DocumentTemplateGroup documentTemplateGroup);

    /**
     * Удалить группу шаблонов документов
     *
     * @param documentTemplateGroup - сущность для сохранения
     */
    void remove(final @NotNull DocumentTemplateGroup documentTemplateGroup);

    /**
     * Получить общее количество элементов
     *
     * @return общее количество
     */
    int getTotalCount(Query<DocumentTemplateGroup, DocumentTemplateGroup> query);
}
