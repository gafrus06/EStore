package ru.isands.test.estore.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final ResourceLoader resourceLoader;

    public ViewController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/items")
    public ResponseEntity<Resource> getItemsPage() {
        Resource resource = resourceLoader.getResource("classpath:static/electroItems.html");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/html")
                .body(resource);
    }
    @GetMapping("/purchase-type")
    public ResponseEntity<Resource> getPurchaseTypePage() {
        Resource resource = resourceLoader.getResource("classpath:static/purchase-type.html");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/html")
                .body(resource);
    }
    @GetMapping("/electro-type")
    public ResponseEntity<Resource> getElectroTypePage() {
        Resource resource = resourceLoader.getResource("classpath:static/electro-type.html");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/html")
                .body(resource);
    }
    @GetMapping("/employee")
    public ResponseEntity<Resource> getEmployeesPage() {
        Resource resource = resourceLoader.getResource("classpath:static/employee.html");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/html")
                .body(resource);
    }
    @GetMapping("/purchase")
    public ResponseEntity<Resource> getPurchasePage() {
        Resource resource = resourceLoader.getResource("classpath:static/purchase.html");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/html")
                .body(resource);
    }
    @GetMapping("/shops")
    public ResponseEntity<Resource> getShopPage() {
        Resource resource = resourceLoader.getResource("classpath:static/shop.html");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/html")
                .body(resource);
    }
    @GetMapping("/electro-shop")
    public ResponseEntity<Resource> getEShopPage() {
        Resource resource = resourceLoader.getResource("classpath:static/electro-shop.html");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/html")
                .body(resource);
    }
    @GetMapping("/position-type")
    public ResponseEntity<Resource> getPosTypePage() {
        Resource resource = resourceLoader.getResource("classpath:static/positionType.html");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/html")
                .body(resource);
    }
    @GetMapping("/electro-employee")
    public ResponseEntity<Resource> getElectroEmployeePage() {
        Resource resource = resourceLoader.getResource("classpath:static/electro-employee.html");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/html")
                .body(resource);
    }
}
