package com.mindex.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.Compensation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Component
public class DataBootstrap {
    private static final String EMPLOYEE_DATASTORE_LOCATION = "/static/employee_database.json";
    private static final String COMPENSATION_DATASTORE_LOCATION = "/static/compensation_database.json";
    private static final Logger LOG = LoggerFactory.getLogger(DataBootstrap.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        InputStream employeeInputStream = this.getClass().getResourceAsStream(EMPLOYEE_DATASTORE_LOCATION);
        InputStream compensationInputStream = this.getClass().getResourceAsStream(COMPENSATION_DATASTORE_LOCATION);
        Employee[] employees;
        Compensation[] compensationArray;

        try {
            employees = objectMapper.readValue(employeeInputStream, Employee[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            LOG.warn("Reading Compensation");
            compensationArray = objectMapper.readValue(compensationInputStream, Compensation[].class);
            LOG.warn("Read Compensation " + compensationArray.length);

        } catch (IOException e) {
            LOG.warn("Error loading compensation");
            throw new RuntimeException(e);
        }

        for (Employee employee : employees) {
            employeeRepository.insert(employee);
        }
        for(Compensation compensation: compensationArray){
            LOG.warn("Loading compensation " + compensation);
            compensationRepository.insert(compensation);
        }
    }
}
