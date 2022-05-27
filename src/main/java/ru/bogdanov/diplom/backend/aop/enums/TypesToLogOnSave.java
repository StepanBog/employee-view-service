package ru.bogdanov.diplom.backend.aop.enums;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.bogdanov.diplom.backend.data.containers.Employee;
import ru.bogdanov.diplom.backend.data.containers.Employer;
import ru.bogdanov.diplom.backend.data.containers.Salary;
import ru.bogdanov.diplom.backend.data.containers.User;

import java.time.LocalDateTime;

public enum TypesToLogOnSave {
    EMPLOYEE {
        @Override
        public String logSaveMethod(Object obj) {
            Employee el = (Employee) obj;
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return "+++ At " + LocalDateTime.now()
                    + " User: " + principal.getUsername() + " with id: " + principal.getId()
                    + " saved an employee with id: " + el.getId()
                    + " and the corresponding employer's id: " + el.getEmployerId();
        }
    },
    EMPLOYER {
        @Override
        public String logSaveMethod(Object obj) {
            Employer el = (Employer) obj;
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return "+++ At " + LocalDateTime.now()
                    + " User: " + principal.getUsername() + " with id: " + principal.getId()
                    + " saved an employer with id: " + el.getId();
        }
    },
    SALARY {
        @Override
        public String logSaveMethod(Object obj) {
            Salary el = (Salary) obj;
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return "+++ At " + LocalDateTime.now()
                    + " User: " + principal.getUsername() + " with id: " + principal.getId()
                    + " saved salary with id: " + el.getId();
        }
    },
    USER {
        @Override
        public String logSaveMethod(Object obj) {
            User el = (User) obj;
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return "+++  At " + LocalDateTime.now()
                    + " User: " + principal.getUsername() + " with id: " + principal.getId()
                    + " saved user with id: " + el.getId()
                    + " and the corresponding employee's id is: " + el.getEmployeeId()
                    + " and the corresponding employer's id is: " + el.getEmployerId();
        }
    };

    public abstract String logSaveMethod(Object obj);
}
