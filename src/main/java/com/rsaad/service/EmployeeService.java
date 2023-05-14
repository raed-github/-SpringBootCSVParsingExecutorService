package com.rsaad.service;

import com.rsaad.entity.Employee;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface EmployeeService {
    public CompletableFuture<List<Employee>> saveEmployees(MultipartFile file) throws Exception;
    public CompletableFuture<List<Employee>> findAllEmployees();
}
