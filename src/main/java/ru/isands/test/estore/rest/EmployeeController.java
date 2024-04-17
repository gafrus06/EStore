package ru.isands.test.estore.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Employee", description = "Сервис для выполнения операций над сотрудниками магазина")
@RequestMapping("/estore/api/employee")
public class EmployeeController {
	
	@GetMapping("/test")
	@Operation(summary = "Тестовый метод", responses = {
		@ApiResponse(description = "Тестовая фраза")
	})
	public ResponseEntity<String> test() {
		return ResponseEntity.ok("test");
	}
}
