package tech.inno.odp.backend.data.containers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Position {

    private String id;

    /**
     * Признак того что позиция сотрудника прошла проверку по чек листу
     */
    private boolean isCheckedByList;

    /**
     * Зарплаты работника
     */
    private List<Salary> salaries = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
