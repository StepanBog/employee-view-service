package tech.inno.odp.backend.service;

import tech.inno.odp.backend.data.containers.ServiceStopInterval;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author VKozlov
 * Сервис для работы со стоп интервалами
 */
public interface IServiceStopIntervalService {

    /**
     * Найти стоп интервалы
     *
     * @param employeeId - идентификатор работника
     * @return список стоп интервалов
     */
    List<ServiceStopInterval> findByEmployeeId(final @NotNull UUID employeeId);

    /**
     * Сохранить
     *
     * @param serviceStopInterval - сущность для сохранения
     */
    ServiceStopInterval save(final @NotNull ServiceStopInterval serviceStopInterval);
}
