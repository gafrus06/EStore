package ru.isands.test.estore.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dao.dto.ElectroShopDTO;
import ru.isands.test.estore.dao.entity.ElectroShop;
import ru.isands.test.estore.service.ElectroShopService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "ElectroShop", description = "Сервис для выполнения операций над магазинами и их товарами")
@RequestMapping("/estore/api/electro-shop")
public class ElectroShopController {
    private final ElectroShopService electroShopService;
    @Autowired
    public ElectroShopController(ElectroShopService electroShopService) {
        this.electroShopService = electroShopService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ElectroShopDTO>> getAllElectroShops() {
        List<ElectroShopDTO> electroShops = electroShopService.getAllElectroShopDTO();
        return ResponseEntity.ok(electroShops);
    }

    @GetMapping("/{shopId}/items")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ElectroShopDTO>> getItemsByShopId(@PathVariable Long shopId) {
        List<ElectroShopDTO> items = electroShopService.getItemsByShopId(shopId);
        return ResponseEntity.ok(items);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ElectroShopDTO> createElectroShop(@Valid @RequestBody ElectroShopDTO electroShopDTO) {
        ElectroShopDTO createdElectroShop = electroShopService.create(electroShopDTO);
        return ResponseEntity.ok(createdElectroShop);
    }

    @PutMapping("/{shopId}/{electroItemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ElectroShopDTO> updateElectroShop(
            @PathVariable Long shopId,
            @PathVariable Long electroItemId,
            @Valid @RequestBody ElectroShopDTO electroShopDTO) {
        ElectroShopDTO updatedElectroShop = electroShopService.update(shopId, electroItemId, electroShopDTO);
        return ResponseEntity.ok(updatedElectroShop);
    }

    @PostMapping("/import")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> importCSV(@RequestParam("file") MultipartFile file) {
        return electroShopService.importFromCsv(file);
    }
}

