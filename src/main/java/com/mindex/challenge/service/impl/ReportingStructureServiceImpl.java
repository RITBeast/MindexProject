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

    private static int reportCounter =0;
    @Override
    public ReportingStructure getReports(String id) {
        //We may need a semaphore here? Seems to be working without it, but if we start getting odd
        //numbers out of the reportCounter or concurrency issues, uncommenting this might be a start.
       /* while(ReportingStructureServiceImpl.reportCounter != 0){
            try {
                wait(100);
            } catch (InterruptedException e){

            }
        }*/
        ReportingStructure completeReportingStructure = generateReportTreeForEmployee(id);
        ReportingStructureServiceImpl.reportCounter = 0;
        return completeReportingStructure;


    }

    private ReportingStructure generateReportTreeForEmployee(String id){
        LOG.debug("Getting employee RS [{}]", id);
        //get and set the employee
        Employee employee = employeeRepository.findByEmployeeId(id);
        ReportingStructure reports = new ReportingStructure();
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
        reports.setEmployee(employee);

        LOG.debug("Set employee");
        ArrayList<Employee> loadedReports = new ArrayList<>();

        if(employee.getDirectReports() !=null) {
            //for each direct report, pull their data and load their reports (if any), count as we go and add the Employee and their reports as we
            //pop back up the recursion stack.
            for (Employee report : employee.getDirectReports()) {

                ReportingStructureServiceImpl.reportCounter++;
                LOG.debug("Getting employee RS [{}]", report.getEmployeeId());
                ReportingStructure singleEmployeeStructure = generateReportTreeForEmployee(report.getEmployeeId());
                loadedReports.add(singleEmployeeStructure.getEmployee());
            }
            employee.setDirectReports(loadedReports);
        }
        reports.setNumberOfReports(ReportingStructureServiceImpl.reportCounter);

        return reports;
    }
}
