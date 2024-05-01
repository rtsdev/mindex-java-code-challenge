package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testReadReportingStructure() throws Exception {
        String id = "16a596ae-edd3-4847-99fe-c4518e82c86f";
        String baseUrl = "http://localhost:" + port + "/reportingStructure/{id}";

        ResponseEntity<ReportingStructure> response = restTemplate.getForEntity(baseUrl, ReportingStructure.class, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmployee().getEmployeeId()).isEqualTo(id);
        assertThat(response.getBody().getNumberOfReports()).isNotNegative();
    }

    @Test
    public void testReadReportingStructureNotFound() {
        String id = "99999";
        String baseUrl = "http://localhost:" + port + "/reportingStructure/{id}";

        ResponseEntity<Void> response = restTemplate.getForEntity(baseUrl, Void.class, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
