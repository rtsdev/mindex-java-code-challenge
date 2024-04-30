package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Getting reporting structure for employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(getNumberOfReports(employee));

        return reportingStructure;
    }

    private int getNumberOfReports(Employee employee) {
        int numberOfReports = 0;

        if (employee.getDirectReports() != null) {
            numberOfReports += employee.getDirectReports().size();

            for (Employee directReport : employee.getDirectReports()) {
                // I wouldn't nest this query in a loop like this, but the demo app isn't set up to fetch the complete
                // Employee association for the directReports property of a given employee
                Employee subReport = employeeRepository.findByEmployeeId(directReport.getEmployeeId());
                numberOfReports += getNumberOfReports(subReport);
            }
        }

        return numberOfReports;
    }
}
