package com.mindex.challenge.data;

import org.springframework.data.annotation.PersistenceConstructor;

import java.time.LocalDate;
import java.util.UUID;


public class Compensation{

    private Double salary;
    private String employeeId;
    private LocalDate effectiveDate;

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String toString(){
        return "EmployeeId: " + employeeId + " Salary: " + this.salary + ", effectiveDate: " + this.effectiveDate;
    }
    @Override
    public boolean equals(Object o){
        if (getClass() != o.getClass()) {
            return false;
        }
        Compensation passedObj = (Compensation) o;
        if(this.getSalary().equals(passedObj.getSalary())
                && this.effectiveDate.equals(passedObj.getEffectiveDate())
                && this.employeeId.equals(passedObj.getEmployeeId())){
            return true;
        }
        return false;
    }

}