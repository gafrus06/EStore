package ru.isands.test.estore.service;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dao.dto.ElectroShopDTO;
import ru.isands.test.estore.dao.entity.ElectroItem;
import ru.isands.test.estore.dao.entity.ElectroShop;
import ru.isands.test.estore.dao.entity.Shop;
import ru.isands.test.estore.dao.entity.pk.ElectroShopPK;
import ru.isands.test.estore.dao.repo.ElectroItemRepository;
import ru.isands.test.estore.dao.repo.ElectroShopRepository;
import ru.isands.test.estore.dao.repo.ShopRepository;
import ru.isands.test.estore.exception.ServiceException;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ElectroShopService {
    private final ElectroShopRepository electroShopRepository;
    private final ModelMapper modelMapper;
    private final ElectroItemRepository electroItemRepository;
    private final ShopRepository shopRepository;

    @Autowired
    public ElectroShopService(ElectroShopRepository electroShopRepository,
                              ModelMapper modelMapper,
                              ElectroItemRepository electroItemRepository,
                              ShopRepository shopRepository) {
        this.electroShopRepository = electroShopRepository;
        this.modelMapper = modelMapper;
        this.electroItemRepository = electroItemRepository;
        this.shopRepository = shopRepository;
    }

    public List<ElectroShopDTO> getAllElectroShopDTO() {
        List<ElectroShop> eShops = electroShopRepository.findAll();
        return eShops.stream()
                .map(e -> modelMapper.map(e, ElectroShopDTO.class))
                .collect(Collectors.toList());
    }

    public ElectroShopDTO create(@Valid ElectroShopDTO dto) {
        ElectroShop eShop = modelMapper.map(dto, ElectroShop.class);
        eShop.setCount(dto.getCount());
        return modelMapper.map(electroShopRepository.save(eShop), ElectroShopDTO.class);
    }
    public ElectroShopDTO update(Long shopId, Long electroItemId, ElectroShopDTO electroShopDTO) {
        ElectroShop electroShop = electroShopRepository.findById(new ElectroShopPK(shopId, electroItemId))
                .orElseThrow(() -> new ServiceException("ElectroShop not found"));
        modelMapper.map(electroShopDTO, electroShop);
        Shop shop = shopRepository.findById(shopId)
                        .orElseThrow(() -> new ServiceException("Магазин с ID " + shopId + " не найден"));
        ElectroItem electroItem = electroItemRepository.findById(electroItemId)
                        .orElseThrow(() -> new ServiceException("Электротовар с ID " + electroItemId + " не найден"));

        electroShop.setShopId(shop);
        electroShop.setElectroItemId(electroItem);
        ElectroShop updatedEShop= electroShopRepository.save(electroShop);
        log.info("Employee with id " + updatedEShop.getShopId().getId() +
                " " + updatedEShop.getElectroItemId().getId() + " updated!");
        return modelMapper.map(updatedEShop, ElectroShopDTO.class);
    }

    public List<ElectroShopDTO> getItemsByShopId(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ServiceException("Магазина с ID " + shopId + " не найден"));
        List<ElectroShop> electroShops = electroShopRepository.findByShopId(shop);
        return electroShops.stream()
                .map(e -> modelMapper.map(e, ElectroShopDTO.class))
                .collect(Collectors.toList());
    }

    public ResponseEntity<String> importFromCsv(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The file cannot be empty.");
        }
        List<ElectroShopDTO> electroShops = new ArrayList<>();

        try (CSVReader csvReader = new CSVReaderBuilder(
                new InputStreamReader(file.getInputStream(), "windows-1251"))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            String[] nextLine;
            csvReader.readNext();
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length < 3) {
                    continue;
                }
                try {
                    ElectroShopDTO electroShopDTO = ElectroShopDTO.builder()
                            .shopId(Long.parseLong(nextLine[0]))
                            .electroItemId(Long.parseLong(nextLine[1]))
                            .count(Integer.parseInt(nextLine[2]))
                            .build();
                    electroShops.add(electroShopDTO);
                }catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Error in the data format: " + e.getMessage());
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error in data processing: " + e.getMessage());
                }
            }
            for (ElectroShopDTO e : electroShops) {
                create(e);
            }

            return ResponseEntity.ok("Данные успешно импортированы!");

        } catch (IOException | CsvException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("\n" +
                    "Error processing the file: " + e.getMessage());
        }
    }

}
