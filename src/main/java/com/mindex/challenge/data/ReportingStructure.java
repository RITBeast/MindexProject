package com.mindex.challenge.data;

import java.util.List;

public class ReportingStructure{
    Employee employee;
    private int numberOfReports = 0;

    public int getNumberOfReports() {
        return numberOfReports;
    }

    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

}