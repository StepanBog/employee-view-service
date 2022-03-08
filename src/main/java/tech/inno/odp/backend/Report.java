package tech.inno.odp.backend;

import java.time.LocalDate;

public class Report {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double balance;

    public Report(Long id, String source, String name, LocalDate startDate,
                  LocalDate endDate, Double balance) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Double getBalance() {
        return balance;
    }

}
