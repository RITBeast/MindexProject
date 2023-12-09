package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompensationController {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;

    @PostMapping("/compensation")
    public List<Compensation> create(@RequestBody Compensation compensation) {

        //Requirements are really unclear how we want this stored.
        //Effective date implies we want multiple copies to co-exist in the DB
        //No deletion implies we want to update one entry.
        //I went with the option that allows us to fix it later and keeps all the data.
        LOG.debug("Received compensation create request for [{}]", compensation);
        return compensationService.create(compensation);
    }

    @GetMapping("/compensation/{id}")
    public List<Compensation> read(@PathVariable String id) {
        LOG.debug("Received compensation read request for id [{}]", id);
        return compensationService.read(id);
    }

}
