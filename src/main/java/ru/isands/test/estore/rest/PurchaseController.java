package ru.isands.test.estore.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dao.dto.EmployeeDTO;
import ru.isands.test.estore.dao.dto.PurchaseDTO;
import ru.isands.test.estore.service.PurchaseService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "Purchase", description = "Сервис для выполнения операций над покупками магазина")
@RequestMapping("/estore/api/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PurchaseDTO>> getAllPurchases(
            @RequestParam(value = "sort", required = false, defaultValue = "id") String sort,
            @RequestParam(value = "direction", required = false, defaultValue = "asc") String direction) {
        List<PurchaseDTO> purchases = purchaseService.getAll(sort, direction);
        return ResponseEntity.ok(purchases);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PurchaseDTO> get(@PathVariable Long id){
        return purchaseService.getById(id);
    }
    @PostMapping("/import")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> importCSV(@RequestParam("file") MultipartFile file){
        return purchaseService.importFromCsv(file);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PurchaseDTO> createPurchase(@Valid @RequestBody PurchaseDTO purchaseDTO) {
        PurchaseDTO createdPurchases = purchaseService.create(purchaseDTO);
        return ResponseEntity.ok(createdPurchases);
    }
    @GetMapping("/calculate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getMoneyThroughCash(){
        return ResponseEntity.ok(purchaseService.calculateMoneyThroughCash());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<PurchaseDTO> updatePurchase(@PathVariable Long id, @Valid @RequestBody PurchaseDTO purchaseDTO) {
        PurchaseDTO updatedPurchase = purchaseService.update(id, purchaseDTO);
        return ResponseEntity.ok(updatedPurchase);
    }

}
