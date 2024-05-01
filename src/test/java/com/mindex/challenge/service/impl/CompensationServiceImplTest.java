package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import java.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateCompensation() {
        String baseUrl = "http://localhost:" + port + "/compensation";

        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        Compensation compensation = new Compensation();
        compensation.setEmployee(testEmployee);
        compensation.setSalary(100000);
        compensation.setEffectiveDate(LocalDate.parse("2024-01-01"));

        ResponseEntity<Compensation> response = restTemplate.postForEntity(baseUrl, compensation, Compensation.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getSalary()).isEqualTo(100000);
        assertThat(response.getBody().getEffectiveDate()).isEqualTo("2024-01-01");
    }

    @Test
    public void testReadCompensationNotFound() {
        // Arrange
        String baseUrl = "http://localhost:" + port + "/compensation/";
        String nonExistentId = "99999";

        // Act and Assert
        try {
            restTemplate.getForEntity(baseUrl + nonExistentId, Compensation.class);
        } catch (HttpClientErrorException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
