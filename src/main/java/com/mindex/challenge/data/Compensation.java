package com.mindex.challenge.data;

import java.time.LocalDateTime;

public class Compensation{

    private Double Salary;
    private String employeeId;
    private LocalDateTime effectiveDate;

    public Double getSalary() {
        return Salary;
    }

    public void setSalary(Double salary) {
        Salary = salary;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployee(String employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }


}