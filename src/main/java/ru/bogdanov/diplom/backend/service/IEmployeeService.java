package ru.bogdanov.diplom.backend.service;

import com.vaadin.flow.data.provider.Query;
import ru.bogdanov.diplom.backend.data.containers.Employee;
import ru.bogdanov.diplom.backend.data.containers.Employer;
import ru.bogdanov.diplom.grpc.generated.EmployeeStatus;
import ru.bogdanov.diplom.grpc.generated.service.employee.EmployeesResponse;
import ru.bogdanov.diplom.grpc.generated.service.employee.SearchEmployeeRequest;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author SBogdanov
 * Сервис для работы с работниками
 */
public interface IEmployeeService {

    /**
     * Найти работников
     *
     * @param request - критерии поиска
     * @return - список работников
     */
    EmployeesResponse find(final @NotNull SearchEmployeeRequest request);

    /**
     * Найти работников
     *
     * @param query    - критерии поиска
     * @param pageSize - количество элементов выводимых на 1й странице
     * @return - список работников
     */
    List<Employee> find(Query<Employee, Employee> query, int pageSize);

    /**
     * Найти работника по id
     *
     * @param employeeId - id работника
     * @return -  работник
     */
    Employee findById(final @NotNull UUID employeeId);

    /**
     * Сохранить работника
     *
     * @param employee - сущность работника
     */
    Employee save(final @NotNull Employee employee);

    /**
     * Получить общее количество элементов
     *
     * @return общее количество
     */
    int getTotalCount(Query<Employee, Employee> query);

    /**
     * Получение количества работников со определенным статусом у конкретного работодателя
     *
     * @param employer работодатель
     * @param status статус работника
     * @return количество
     */
    long countEmployeesByEmployer(@NotNull Employer employer, EmployeeStatus status);

    /**
     * Получение количества работников у конкретного работодателя
     *
     * @param employer работодатель
     * @return количество
     */
    long countEmployeesByEmployer(@NotNull Employer employer);
}
