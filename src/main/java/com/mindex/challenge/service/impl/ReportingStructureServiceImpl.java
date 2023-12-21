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

        ArrayList<String> seenIds = new ArrayList<>();
        seenIds.add(id);

        return generateReportTreeForEmployee(id, seenIds);

    }

    private ReportingStructure generateReportTreeForEmployee(String id, ArrayList<String> seenIds){
        LOG.debug("Getting employee RS [{}]", id);
        Employee employee = employeeRepository.findByEmployeeId(id);
        ReportingStructure reports = new ReportingStructure();
        ArrayList<Employee> loadedReports = new ArrayList<>();
        int counter = 0; //track each individual level of reports with the counter

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
        reports.setEmployee(employee);

        if(employee.getDirectReports() !=null) {

            //for each direct report, pull their data and load their reports (if any), count as we go and add the Employee and their reports as we
            //pop back up the recursion stack.
            for (Employee report : employee.getDirectReports()) {
                //avoid potential infinite loop, just ignore this employee if we've seen them before
                if(!seenIds.contains(report.getEmployeeId())) {

                    LOG.debug("Getting employee RS [{}]", report.getEmployeeId());
                    seenIds.add(report.getEmployeeId());
                    ReportingStructure singleEmployeeStructure = generateReportTreeForEmployee(report.getEmployeeId(), seenIds);

                    loadedReports.add(singleEmployeeStructure.getEmployee());
                    //count direct reports' direct reports
                    counter += singleEmployeeStructure.getNumberOfReports() + 1;

                }
            }
            employee.setDirectReports(loadedReports);
        }
        reports.setNumberOfReports(counter);

        return reports;
    }
}
