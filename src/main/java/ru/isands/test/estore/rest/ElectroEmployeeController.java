package ru.isands.test.estore.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dao.dto.ElectroEmployeeDTO;
import ru.isands.test.estore.dao.dto.ElectroShopDTO;
import ru.isands.test.estore.service.ElectroEmployeeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "ElectroEmployee", description = "Сервис для выполнения операций над вспомогательной связью магазина")
@RequestMapping("/estore/api/electro-employee")
public class ElectroEmployeeController {
    private final ElectroEmployeeService electroEmployeeService;

    @Autowired
    public ElectroEmployeeController(ElectroEmployeeService electroEmployeeService) {
        this.electroEmployeeService = electroEmployeeService;
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ElectroEmployeeDTO>> getAllElectroEmployees() {
        return ResponseEntity.ok(electroEmployeeService.getAll());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ElectroEmployeeDTO> createElectroEmployee(@Valid @RequestBody ElectroEmployeeDTO dto) {
        ElectroEmployeeDTO created = electroEmployeeService.create(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{shopId}/{electroItemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ElectroEmployeeDTO> updateElectroEmployee(
            @PathVariable Long shopId,
            @PathVariable Long electroItemId,
            @Valid @RequestBody ElectroEmployeeDTO dto) {
        ElectroEmployeeDTO updated = electroEmployeeService.update(shopId, electroItemId, dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/import")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> importCSV(@RequestParam("file") MultipartFile file) {
        return electroEmployeeService.importFromCsv(file);
    }
}
