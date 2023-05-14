package com.rsaad.service;

import com.rsaad.entity.Employee;
import com.rsaad.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    @Autowired
    private EmployeeRepository employeeRepository;

    Object traget;
    Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Async
    public CompletableFuture<List<Employee>> saveEmployees(MultipartFile file) throws Exception {
        long start = System.currentTimeMillis();
        List<Employee> employees = parseCSVFile(file);
        logger.info("saving list of users of size {}", employees.size(), "" + Thread.currentThread().getName());
        employees = employeeRepository.saveAll(employees);
        long end = System.currentTimeMillis();
        logger.info("Total time {}", (end - start));
        return CompletableFuture.completedFuture(employees);
    }

    @Async
    public CompletableFuture<List<Employee>> findAllEmployees(){
        logger.info("get list of user by "+Thread.currentThread().getName());
        List<Employee> employees=employeeRepository.findAll();
        return CompletableFuture.completedFuture(employees);
    }
    private List<Employee> parseCSVFile(final MultipartFile file) throws Exception {
        final List<Employee> employees = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(",");
                    final Employee employee = new Employee();
                    employee.setFirstName(data[0]);
                    employee.setLastName(data[1]);
                    employee.setEmail(data[2]);
                    employee.setGender(data[3]);
                    employee.setIpAddress(data[4]);
                    employees.add(employee);
                }
                return employees;
            }
        } catch (final IOException e) {
            logger.error("Failed to parse CSV file {}", e);
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }
}
