package ru.isands.test.estore.service;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dao.dto.PurchaseDTO;
import ru.isands.test.estore.dao.entity.*;
import ru.isands.test.estore.dao.entity.pk.ElectroShopPK;
import ru.isands.test.estore.dao.repo.*;
import ru.isands.test.estore.exception.ServiceException;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final ElectroShopRepository electroShopRepository;
    private final ModelMapper modelMapper;
    private final ElectroItemRepository electroItemRepository;
    private final EmployeeRepository employeeRepository;
    private final ShopRepository shopRepository;
    private final PurchaseTypeRepository purchaseTypeRepository;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository,
                           ElectroShopRepository electroShopRepository, ModelMapper modelMapper,
                           ElectroItemRepository electroItemRepository,
                           EmployeeRepository employeeRepository,
                           ShopRepository shopRepository,
                           PurchaseTypeRepository purchaseTypeRepository) {
        this.purchaseRepository = purchaseRepository;
        this.electroShopRepository = electroShopRepository;
        this.modelMapper = modelMapper;
        this.electroItemRepository = electroItemRepository;
        this.employeeRepository = employeeRepository;
        this.shopRepository = shopRepository;
        this.purchaseTypeRepository = purchaseTypeRepository;
    }


    public List<PurchaseDTO> getAll(String sort, String direction) {
        Sort sortOrder = "desc".equalsIgnoreCase(direction) ?
                Sort.by(sort).descending() :
                Sort.by(sort).ascending();
        return purchaseRepository.findAll(sortOrder)
                .stream()
                .map(purchase -> modelMapper.map(purchase, PurchaseDTO.class))
                .collect(Collectors.toList());
    }

    public List<PurchaseDTO> getAll() {
        return purchaseRepository.findAll()
                .stream()
                .map(purchase -> modelMapper.map(purchase, PurchaseDTO.class))
                .collect(Collectors.toList());
    }

    public PurchaseDTO create(@Valid PurchaseDTO dto) {
        if (isExistElectroItemInShop(dto.getShopId(), dto.getElectroItemId())) {
            log.warn("Электротовара с ID " + dto.getElectroItemId() + " нет в наличии в магазине " + dto.getShopId());
            return null;
        }
        Shop shop = shopRepository.findById(dto.getShopId())
                .orElseThrow(() -> new ServiceException("Магазин с ID " + dto.getShopId() + " не найден"));

        PurchaseType purchaseType = purchaseTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new ServiceException("Тип покупки с ID " + dto.getTypeId() + " не найден"));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ServiceException("Сотрудник с ID " + dto.getEmployeeId() + " не найден"));

        ElectroItem electroItem = electroItemRepository.findById(dto.getElectroItemId())
                .orElseThrow(() -> new ServiceException("Электротовар с ID " + dto.getElectroItemId() + " не найден"));

        Purchase purchase = modelMapper.map(dto, Purchase.class);
        purchase.setShop(shop);
        purchase.setType(purchaseType);
        purchase.setEmployee(employee);
        purchase.setElectroItem(electroItem);

        return modelMapper.map(purchaseRepository.save(purchase), PurchaseDTO.class);
    }


    public PurchaseDTO update(Long id, @Valid PurchaseDTO dto) {
        if (isExistElectroItemInShop(dto.getShopId(), dto.getElectroItemId())) {
            throw new ServiceException("Электротовара с ID "
                    + dto.getElectroItemId()
                    + " нет в наличии в магазине " + dto.getShopId());
        }
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Покупка с ID " + id + " не найдена"));
        modelMapper.map(dto, purchase);
        Shop shop = shopRepository.findById(dto.getShopId())
                .orElseThrow(() -> new ServiceException("Магазин с ID " + id + " не найден"));
        PurchaseType purchaseType = purchaseTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new ServiceException("Тип покупки с ID " + id + " не найден"));
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ServiceException("Сотрудник с ID " + id + " не найден"));
        ElectroItem electroItem = electroItemRepository.findById(dto.getElectroItemId())
                .orElseThrow(() -> new ServiceException("Электротовар с ID " + id + " не найден"));
        purchase.setId(id);
        purchase.setShop(shop);
        purchase.setType(purchaseType);
        purchase.setEmployee(employee);
        purchase.setElectroItem(electroItem);

        log.info("Purchase with id " + purchase.getId() + " updated!");
        return modelMapper.map(purchaseRepository.save(purchase), PurchaseDTO.class);
    }


    public ResponseEntity<PurchaseDTO> getById(Long id) {
        return ResponseEntity.ok(modelMapper.map(purchaseRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Покупка с ID " + id + " не найдена")), PurchaseDTO.class));

    }

    public ResponseEntity<String> importFromCsv(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The file cannot be empty.");
        }
        List<PurchaseDTO> purchases = new ArrayList<>();
        List<Long> notAvailableIds = new ArrayList<>();

        try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream(), "windows-1251"))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            String[] nextLine;
            csvReader.readNext();
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length < 6) {
                    continue;
                }

                Long electroItemId = Long.parseLong(nextLine[1]);
                Long shopId = Long.parseLong(nextLine[5]);
                if (isExistElectroItemInShop(shopId, electroItemId)) {
                    notAvailableIds.add(electroItemId);
                    continue;
                }

                try {
                    PurchaseDTO purchaseDTO = PurchaseDTO.builder()
                            .electroItemId(electroItemId)
                            .employeeId(Long.parseLong(nextLine[2]))
                            .purchaseDate(parsePurchaseDate(nextLine[3]))
                            .typeId(Long.parseLong(nextLine[4]))
                            .shopId(shopId)
                            .build();
                    purchases.add(purchaseDTO);
                } catch (NumberFormatException e) {
                    return ResponseEntity.badRequest().body("Error in the data format: " + e.getMessage());
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Error in data processing: " + e.getMessage());
                }
            }

            for (PurchaseDTO purchase : purchases) {
                create(purchase);
            }

            StringBuilder message = new StringBuilder("Данные успешно импортированы!");
            if (!notAvailableIds.isEmpty()) {
                message.append(" Товары, которых нет в наличии в магазине: ");
                message.append(notAvailableIds.stream().map(String::valueOf).collect(Collectors.joining(", ")));
            }

            return ResponseEntity.ok(message.toString());

        } catch (IOException | CsvException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file: " + e.getMessage());
        }
    }


    private static LocalDateTime parsePurchaseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            throw new IllegalArgumentException("Date string must not be null or empty.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        try {
            return LocalDateTime.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный формат времени ", e);
        }
    }
    private boolean isExistElectroItemInShop(Long shopId, Long electroItemId) {
        ElectroShopPK electroShopPK = new ElectroShopPK(shopId, electroItemId);
        Optional<ElectroShop> eShopOptional = electroShopRepository.findById(electroShopPK);
        if (eShopOptional.isPresent()) {
            ElectroShop eShop = eShopOptional.get();
            return eShop.getCount() <= 0;
        }
        return true;
    }
    public String calculateMoneyThroughCash(){
        return String.valueOf(purchaseRepository.calculateMoneyThroughCash());
    }

}
