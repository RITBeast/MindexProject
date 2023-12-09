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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

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

        //Check Creation
        List<Compensation> createdCompensationList = Arrays.asList(restTemplate.postForEntity(compensationUrl, testComp, Compensation[].class).getBody());

        assertNotNull(createdCompensationList);
        assertEquals(1, createdCompensationList.size());
        assertEquals(testComp, createdCompensationList.get(0));

        //Check Read
        List<Compensation> readCompensationList = Arrays.asList(restTemplate.getForObject(compensationIdUrl, Compensation[].class, testComp.getEmployeeId()));
        assertNotNull(readCompensationList);
        assertEquals(1, readCompensationList.size());
        assertEquals(testComp, readCompensationList.get(0));

    }

    @Test
    public void testMultipleCreate() {
        Compensation testComp = new Compensation();
        testComp.setEmployeeId("Testy Testerson III"); //Ringo doesn't have an entry, so we can play with this
        testComp.setSalary(12.50);
        testComp.setEffectiveDate(LocalDate.now());

        Compensation testComp2 = new Compensation();
        testComp2.setEmployeeId("Testy Testerson III"); //Ringo doesn't have an entry, so we can play with this
        testComp2.setSalary(15.50);

        testComp2.setEffectiveDate(LocalDate.now().plusDays(15));

        List<Compensation> createdCompensationList = Arrays.asList(restTemplate.postForEntity(compensationUrl, testComp, Compensation[].class).getBody());

        assertNotNull(createdCompensationList);
        assertEquals(1, createdCompensationList.size());
        List<Compensation> createdCompensationList2 = Arrays.asList(restTemplate.postForEntity(compensationUrl, testComp2, Compensation[].class).getBody());

        assertNotNull(createdCompensationList2);
        assertEquals(2, createdCompensationList2.size());
        assertTrue(createdCompensationList.contains(testComp));
        assertTrue(createdCompensationList2.contains(testComp));
        assertTrue(createdCompensationList2.contains(testComp2));

    }
}
