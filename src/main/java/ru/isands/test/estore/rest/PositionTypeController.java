package ru.isands.test.estore.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dao.dto.PositionTypeDTO;
import ru.isands.test.estore.service.PositionTypeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "PositionType", description = "Сервис для выполнения операций над должностями сотрудников магазина")
@RequestMapping("/estore/api/position-type")
public class PositionTypeController {
    private final PositionTypeService positionTypeService;

    @Autowired
    public PositionTypeController(PositionTypeService positionTypeService) {
        this.positionTypeService = positionTypeService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<PositionTypeDTO>> getAllTypes() {
        List<PositionTypeDTO> positionTypeDTOs = positionTypeService.getAll();
        return ResponseEntity.ok(positionTypeDTOs);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PositionTypeDTO> createType(@Valid @RequestBody PositionTypeDTO positionTypeDTO) {
        PositionTypeDTO createdType = positionTypeService.create(positionTypeDTO);
        return ResponseEntity.ok(createdType);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<PositionTypeDTO> updateType(@PathVariable Long id, @Valid @RequestBody PositionTypeDTO positionTypeDTO) {
        PositionTypeDTO updatedType = positionTypeService.update(id, positionTypeDTO);
        return ResponseEntity.ok(updatedType);
    }
    @PostMapping("/import")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> importCSV(@RequestParam("file") MultipartFile file){
        return positionTypeService.importFromCsv(file);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PositionTypeDTO> getType(@PathVariable Long id){
        return positionTypeService.getById(id);
    }

}
