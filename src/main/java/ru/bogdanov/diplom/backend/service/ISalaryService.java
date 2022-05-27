package ru.bogdanov.diplom.backend.service;

import ru.bogdanov.diplom.backend.data.containers.Salary;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author SBogdanov
 * Сервис для работы с зарплатами работников
 */
public interface ISalaryService {

    /**
     * Найти Зарплату по id работника
     *
     * @param employeeId - id работника
     * @return -  Зарплата
     */
    Salary findByEmployeeId(final @NotNull UUID employeeId);

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
