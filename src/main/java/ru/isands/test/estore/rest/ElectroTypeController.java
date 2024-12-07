package ru.isands.test.estore.rest;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dao.dto.ElectroItemDTO;
import ru.isands.test.estore.dao.dto.ElectroTypeDTO;
import ru.isands.test.estore.dao.dto.EmployeeDTO;
import ru.isands.test.estore.service.ElectroTypeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "ElectroType", description = "Сервис для выполнения операций над типами электротоваров")
@RequestMapping("/estore/api/electro-type")
public class ElectroTypeController {
    private final ElectroTypeService electroTypeService;

    @Autowired
    public ElectroTypeController(ElectroTypeService electroTypeService) {
        this.electroTypeService = electroTypeService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ElectroTypeDTO> getType(@PathVariable Long id){
        return electroTypeService.getById(id);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<ElectroTypeDTO>> getAllTypes() {
        List<ElectroTypeDTO> electroTypeDTOS = electroTypeService.getAll();
        return ResponseEntity.ok(electroTypeDTOS);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ElectroTypeDTO> createType(@Valid @RequestBody ElectroTypeDTO electroTypeDTO) {
        ElectroTypeDTO createdType = electroTypeService.create(electroTypeDTO);
        return ResponseEntity.ok(createdType);
    }
    @PostMapping("/import")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> importCSV(@RequestParam("file") MultipartFile file) {
        return electroTypeService.importFromCsv(file);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ElectroTypeDTO> updateType(@PathVariable Long id, @Valid @RequestBody ElectroTypeDTO electroTypeDTO) {
        ElectroTypeDTO updatedType = electroTypeService.update(id, electroTypeDTO);
        return ResponseEntity.ok(updatedType);
    }
}
