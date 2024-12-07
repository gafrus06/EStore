package ru.isands.test.estore.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dao.dto.EmployeeDTO;
import ru.isands.test.estore.dao.dto.ShopDTO;
import ru.isands.test.estore.service.ShopService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "Shop", description = "Сервис для выполнения операций над списком магазинов")
@RequestMapping("/estore/api/shop")
public class ShopController {

    private final ShopService shopService;

    @Autowired
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<ShopDTO>> getAllShops() {
        List<ShopDTO> shops = shopService.getAll();
        return ResponseEntity.ok(shops);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ShopDTO> createShop(@Valid @RequestBody ShopDTO shopDTO) {
        ShopDTO createdShop = shopService.create(shopDTO);
        return ResponseEntity.ok(createdShop);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ShopDTO> updateShop(@PathVariable Long id, @Valid @RequestBody ShopDTO shopDTO) {
        ShopDTO updatedShop = shopService.update(id, shopDTO);
        return ResponseEntity.ok(updatedShop);
    }

    @PostMapping("/import")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> importCSV(@RequestParam("file") MultipartFile file){
        return shopService.importFromCsv(file);
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ShopDTO> get(@PathVariable Long id){
        return shopService.getById(id);
    }
}
