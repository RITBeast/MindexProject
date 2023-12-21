package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class CompensationServiceImplTest {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CompensationServiceImplTest.class);
    private String compensationUrl;
    private String compensationIdUrl;

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
        testComp.setEmployeeId("b7839309-3348-463b-a7e3-5de1c168beb3"); //McCartney
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
        testComp.setEmployeeId("03aa1462-ffa9-4978-901b-7c001562cf6f"); //Ringo
        testComp.setSalary(12.50);
        testComp.setEffectiveDate(LocalDate.now());

        Compensation testComp2 = new Compensation();
        testComp2.setEmployeeId("03aa1462-ffa9-4978-901b-7c001562cf6f"); //Ringo
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
    @Test(expected = RuntimeException.class)
    public void testCreateFailure() {
        Compensation testComp = new Compensation();
        testComp.setEmployeeId("Testy Testerson"); //Does not exist
        testComp.setSalary(12.50);
        testComp.setEffectiveDate(LocalDate.now());

        //This should throw a runtime exception
        List<Compensation> createdCompensationList = Arrays.asList(restTemplate.postForEntity(compensationUrl, testComp, Compensation[].class).getBody());


    }
}
