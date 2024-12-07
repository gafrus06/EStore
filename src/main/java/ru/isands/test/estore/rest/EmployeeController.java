package ru.isands.test.estore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dao.dto.EmployeeDTO;
import ru.isands.test.estore.service.EmployeeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "Employee", description = "Сервис для выполнения операций над сотрудниками магазина")
@RequestMapping("/estore/api/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployeeDTO();
        return ResponseEntity.ok(employees);
    }
    @GetMapping("/best-junior")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EmployeeDTO> getBestJunior() {
        EmployeeDTO employees = employeeService.getBestJunior();
        return ResponseEntity.ok(employees);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO createdEmployee = employeeService.create(employeeDTO);
        return ResponseEntity.ok(createdEmployee);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO updatedEmployee = employeeService.update(id, employeeDTO);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PostMapping("/import")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> importCSV(@RequestParam("file")MultipartFile file){
        return employeeService.importFromCsv(file);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EmployeeDTO> get(@PathVariable Long id){
        return ResponseEntity.ok(employeeService.getById(id));
    }
    @GetMapping("/best-sales")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<EmployeeDTO>> getBestBySales(){
        return ResponseEntity.ok(employeeService.getBestEmployeesBySales());
    }
    @GetMapping("/best-sum-items")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<EmployeeDTO>> getBestBySumItems(){
        return ResponseEntity.ok(employeeService.getBestEmployeesBySumItems());
    }

}
