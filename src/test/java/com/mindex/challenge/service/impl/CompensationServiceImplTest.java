package com.mindex.challenge.service.impl;

import com.mindex.challenge.controller.CompensationController;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class CompensationServiceImplTest {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CompensationServiceImplTest.class);
    private String compensationUrl;
    private String compensationIdUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateRead() {
        Compensation testComp = new Compensation();
        testComp.setEmployeeId("Testy Testerson"); //Ringo doesn't have an entry, so we can play with this
        testComp.setSalary(12.50);
        testComp.setEffectiveDate(LocalDate.now());

        // Create checks - Not a big fan of this, but since we're returning a List, I was having trouble getting that to the Compensation Object.
        Compensation createdCompensation = (Compensation) restTemplate.postForEntity(compensationUrl, testComp, List.class).getBody().get(0);

        assertNotNull(createdCompensation);
        assertEquals(testComp.getSalary(), createdCompensation.getSalary());
        assertEquals(testComp.getEffectiveDate(), createdCompensation.getEffectiveDate());
        assertEquals(testComp.getEmployeeId(), createdCompensation.getEmployeeId());


        // Read checks
        LOG.debug("Testing response: " + restTemplate.getForObject(compensationIdUrl, List.class, testComp.getEmployeeId()));
        Compensation readCompensation = (Compensation) restTemplate.getForObject(compensationIdUrl, List.class, testComp.getEmployeeId()).get(0);
        assertNotNull(readCompensation);
        //assertEquals(1, readCompensationList.size());
        assertEquals(testComp.getSalary(), readCompensation.getSalary());
        assertEquals(testComp.getEffectiveDate(), readCompensation.getEffectiveDate());
        assertEquals(testComp.getEmployeeId(), readCompensation.getEmployeeId());

    }
}
