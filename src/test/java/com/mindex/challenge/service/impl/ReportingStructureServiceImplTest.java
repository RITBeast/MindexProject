package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImplTest.class);

    private String reportStructureWithIdUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {

        reportStructureWithIdUrl = "http://localhost:" + port + "/reportingStructure/{id}";

    }
    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
    @Test
    public void testRetrieveReportingStructureSingle() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("George");
        testEmployee.setLastName("Harrison");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer III");
        testEmployee.setEmployeeId("c0c2293d-16bd-4603-8e08-638a9d18b22c");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ReportingStructure rs =
                restTemplate.getForEntity(
                        reportStructureWithIdUrl,
                        ReportingStructure.class,
                        testEmployee.getEmployeeId()).getBody();
        assertNotNull(rs.getEmployee());
        assertEquals(rs.getEmployee().getEmployeeId(), testEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, rs.getEmployee());
        LOG.debug("number of reports on single test (0):" + rs.getNumberOfReports());
        assertEquals(0, rs.getNumberOfReports());
        assertNull(rs.getEmployee().getDirectReports());
    }

    @Test
    public void testRetrieveReportingStructureMultiLevel() {

    //THIS TEST ALSO DOUBLES TO MAKE SURE WE BREAK OUT OF AN INFINITE LOOP SO LONG AS THERE'S AN EMPLOYEE ID ADDED TO A DIRECT REPORT DOWN THE LINE

        //set the expected data
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Lennon");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Development Manager");
        testEmployee.setEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");

        Employee firstReport = new Employee();
        firstReport.setFirstName("Ringo");
        firstReport.setLastName("Starr");
        firstReport.setDepartment("Engineering");
        firstReport.setPosition("Developer V");
        firstReport.setEmployeeId("03aa1462-ffa9-4978-901b-7c001562cf6f");

        //I have serious questions about how George Harrison ended up reporting to Ringo.
        Employee secondReport = new Employee();
        secondReport.setFirstName("George");
        secondReport.setLastName("Harrison");
        secondReport.setDepartment("Engineering");
        secondReport.setPosition("Developer III");
        secondReport.setEmployeeId("c0c2293d-16bd-4603-8e08-638a9d18b22c");

        //get the data
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ReportingStructure rs =
                restTemplate.getForEntity(
                        reportStructureWithIdUrl,
                        ReportingStructure.class,
                        testEmployee.getEmployeeId()).getBody();

        //Does everything match expectation?
        assertNotNull(rs.getEmployee());
        assertEquals(rs.getEmployee().getEmployeeId(), testEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, rs.getEmployee());
        LOG.debug("number of reports on multiple test (4):" + rs.getNumberOfReports());
        assertEquals(4, rs.getNumberOfReports());
        assertEquals(2,rs.getEmployee().getDirectReports().size());

        //test the reports
        for (Employee report : rs.getEmployee().getDirectReports()) {
            if (report.getEmployeeId().equals(firstReport.getEmployeeId())) {
                assertEquals(2, rs.getEmployee().getDirectReports().size());

                assertEmployeeEquivalence(firstReport, report);

                //2 layers should be enough to confirm the recursive algorithm works
                for (Employee report2 : report.getDirectReports()) {

                    if (report2.getEmployeeId().equals(secondReport.getEmployeeId())) {

                        assertNull(report2.getDirectReports());

                        assertEmployeeEquivalence(secondReport, report2);

                    }
                }
            }

        }

    }


}
