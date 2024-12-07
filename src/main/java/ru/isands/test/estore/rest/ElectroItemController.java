package ru.isands.test.estore.rest;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dao.dto.ElectroItemDTO;
import ru.isands.test.estore.dao.dto.EmployeeDTO;
import ru.isands.test.estore.service.ElectroItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "ElectroItems", description = "Сервис для выполнения операций над электротоварами магазина")
@RequestMapping("/estore/api/items")
public class ElectroItemController {

    private final ElectroItemService electroItemService;

    @Autowired
    public ElectroItemController(ElectroItemService electroItemService) {
        this.electroItemService = electroItemService;
    }
    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<ElectroItemDTO>> getAll() {
        List<ElectroItemDTO> electroItemDTOS = electroItemService.getAll();
        return ResponseEntity.ok(electroItemDTOS);
    }
    @PostMapping("/import")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> importCSV(@RequestParam("file") MultipartFile file) {
        return electroItemService.importFromCsv(file);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ElectroItemDTO> create(@Valid @RequestBody ElectroItemDTO dto) {
        ElectroItemDTO createdType = electroItemService.create(dto);
        return ResponseEntity.ok(createdType);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ElectroItemDTO> update(@PathVariable Long id, @Valid @RequestBody ElectroItemDTO dto) {
        ElectroItemDTO updatedType = electroItemService.update(id, dto);
        return ResponseEntity.ok(updatedType);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ElectroItemDTO> get(@PathVariable Long id){
        return electroItemService.getById(id);
    }
}
