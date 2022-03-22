package tech.inno.odp.backend.service;

import com.vaadin.flow.data.provider.Query;
import tech.inno.odp.backend.data.containers.Employer;
import tech.inno.odp.grpc.generated.service.employer.EmployersResponse;
import tech.inno.odp.grpc.generated.service.employer.SearchEmployerRequest;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author VKozlov
 * Сервис для работы с работодателями
 */
public interface IEmployerService {

    /**
     * Найти работодателей
     *
     * @param request - критерии поиска
     * @return - список работодателей
     */
    EmployersResponse find(final @NotNull SearchEmployerRequest request);

    /**
     * Найти работодателей
     *
     * @param query    - критерии поиска
     * @param pageSize - количество элементов выводимых на 1й странице
     * @return - список работодателей
     */
    List<Employer> find(Query<Employer, Employer> query, int pageSize);

    /**
     * Найти работодателя по id
     *
     * @param employerId - id работодателя
     * @return -  работодатель
     */
    Employer findById(final @NotNull UUID employerId);

    /**
     * Сохранить работодателя
     *
     * @param employer - сущность работодателя
     */
    Employer save(final @NotNull Employer employer);

    /**
     * Получить общее количество элементов
     *
     * @return общее количество
     */
    int getTotalCount(Query<Employer, Employer> query);
}
