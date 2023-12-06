package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure getReports(String id) {

        LOG.debug("Getting employee RS [{}]", id);
        //get the employee
        Employee employee = employeeRepository.findByEmployeeId(id);
        LOG.debug("got employee");
        ReportingStructure reports = new ReportingStructure();
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
        reports.setEmployee(employee);
        LOG.debug("Set employee");
        ArrayList<Employee> loadedReports = new ArrayList<>();
        for (Employee report: employee.getDirectReports()) {

            ReportingStructure.numberOfReports++;
            LOG.debug("Getting employee RS [{}]", report.getEmployeeId());
            ReportingStructure singleEmployeeStructure = getReports(report.getEmployeeId());
            loadedReports.add(singleEmployeeStructure.getEmployee());
        }
        employee.setDirectReports(loadedReports);
        return reports;
    }
}
