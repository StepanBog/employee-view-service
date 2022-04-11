package tech.inno.odp.backend.service;

import tech.inno.odp.backend.data.containers.Salary;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author VKozlov
 * Сервис для работы с зарплатами работников
 */
public interface ISalaryService {

    /**
     * Найти Зарплату по id работника
     *
     * @param employeeId - id работника
     * @return -  Зарплата
     */
    List<Salary> findByEmployeeId(final @NotNull UUID employeeId);

    /**
     * Найти Зарплату по id
     *
     * @param id - id
     * @return -  зарплата
     */
    Salary findById(final @NotNull UUID id);

    /**
     * Сохранить зарплату
     *
     * @param salary - сущность зарплаты
     */
    Salary save(final @NotNull Salary salary);
}
