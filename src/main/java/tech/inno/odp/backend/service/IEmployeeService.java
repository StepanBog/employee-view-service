package tech.inno.odp.backend.service;

import com.vaadin.flow.data.provider.Query;
import tech.inno.odp.backend.data.containers.Employee;
import tech.inno.odp.grpc.generated.service.employee.EmployeesResponse;
import tech.inno.odp.grpc.generated.service.employee.SearchEmployeeRequest;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author VKozlov
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
}
