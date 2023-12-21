package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Compensation> create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);
        //Only create if the employee exists
        if(!(employeeRepository.findByEmployeeId(compensation.getEmployeeId()) == null) ){

            compensationRepository.insert(compensation);
            return this.read(compensation.getEmployeeId());
        } else {
            throw new RuntimeException("Employee does not exist!");
        }
    }

    @Override
    public List<Compensation> read(String id) {

        LOG.debug("Retrieving employee Compensation with id [{}]", id);
        return compensationRepository.findByEmployeeId(id);
    }
}
