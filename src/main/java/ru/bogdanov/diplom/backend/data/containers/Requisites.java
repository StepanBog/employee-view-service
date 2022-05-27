package ru.bogdanov.diplom.backend.data.containers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Requisites {

    private String id;

    /**
     * Cнилс (без проблелов и разделителей)
     */
    private String snils;

    /**
     * Cерия паспорт
     */
    private String passportSeries;

    /**
     * Номер паспорта
     */
    private String passportNumber;

    /**
     * ИНН
     */
    private String inn;

    /**
     * КПП
     */
    private String kpp;

    /**
     * Номер счета в банке-получателе
     */
    private String accountNumber;

    /**
     * БИК банка-получателя
     */
    private String bik;

    /**
     * Имя работника
     */
    private String firstName;

    /**
     * Фамилия работника
     */
    private String lastName;

    /**
     * Отчество работника
     */
    private String patronymicName;

    /**
     * корр счет
     */
    private String corr;

    /**
     * Наименование банка
     */
    private String bankName;



    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
