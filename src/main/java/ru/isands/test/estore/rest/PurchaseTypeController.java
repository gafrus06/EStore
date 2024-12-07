package ru.isands.test.estore.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dao.dto.EmployeeDTO;
import ru.isands.test.estore.dao.dto.PurchaseTypeDTO;
import ru.isands.test.estore.service.PurchaseTypeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "PurchaseType", description = "Сервис для выполнения операций над типами покупок")
@RequestMapping("/estore/api/purchase-type")
public class PurchaseTypeController {

    private final PurchaseTypeService purchaseTypeService;

    @Autowired
    public PurchaseTypeController(PurchaseTypeService purchaseTypeService) {
        this.purchaseTypeService = purchaseTypeService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<PurchaseTypeDTO>> getAllTypes() {
        List<PurchaseTypeDTO> purchaseTypeDTO = purchaseTypeService.getAll();
        return ResponseEntity.ok(purchaseTypeDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PurchaseTypeDTO> createType(@Valid @RequestBody PurchaseTypeDTO purchaseTypeDTO) {
        PurchaseTypeDTO createdType = purchaseTypeService.create(purchaseTypeDTO);
        return ResponseEntity.ok(createdType);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<PurchaseTypeDTO> updateType(@PathVariable Long id, @Valid @RequestBody PurchaseTypeDTO purchaseTypeDTO) {
        PurchaseTypeDTO updatedType = purchaseTypeService.update(id, purchaseTypeDTO);
        return ResponseEntity.ok(updatedType);
    }
    @PostMapping("/import")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> importCSV(@RequestParam("file") MultipartFile file){
        return purchaseTypeService.importFromCsv(file);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PurchaseTypeDTO> get(@PathVariable Long id){
        return purchaseTypeService.getById(id);
    }
}
