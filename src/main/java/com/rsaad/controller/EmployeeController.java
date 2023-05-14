package com.rsaad.controller;

import com.rsaad.entity.Employee;
import com.rsaad.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping(value = "/employees", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity saveEmployees(@RequestParam(value = "files") MultipartFile[] files) throws Exception {
        for (MultipartFile file : files) {
            employeeService.saveEmployees(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/employees", produces = "application/json")
    public CompletableFuture<ResponseEntity> findAllEmployees() {
        return  employeeService.findAllEmployees().thenApply(ResponseEntity::ok);
    }

    /**
     * we are splitting the execution to five different tasks (5 threads)
     * @return
     */
    @GetMapping(value = "/employees-multiple-thread", produces = "application/json")
    public  ResponseEntity getEmployees(){
        CompletableFuture<List<Employee>> employees1=employeeService.findAllEmployees();
        CompletableFuture<List<Employee>> employees2=employeeService.findAllEmployees();
        CompletableFuture<List<Employee>> employees3=employeeService.findAllEmployees();
        CompletableFuture<List<Employee>> employees4=employeeService.findAllEmployees();
        CompletableFuture<List<Employee>> employees5=employeeService.findAllEmployees();
        CompletableFuture.allOf(employees1,employees2,employees3,employees4,employees5).join();
        return  ResponseEntity.status(HttpStatus.OK).build();
    }
}
