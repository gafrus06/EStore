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
import ru.isands.test.estore.dao.dto.ShopDTO;
import ru.isands.test.estore.dao.entity.Shop;
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
public class ShopService{
    private final ShopRepository shopRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public ShopService(ShopRepository shopRepository, ModelMapper modelMapper) {
        this.shopRepository = shopRepository;
        this.modelMapper = modelMapper;
    }


    public List<ShopDTO> getAll(){
        return shopRepository.findAll()
                .stream()
                .map(shop -> modelMapper.map(shop, ShopDTO.class))
                .collect(Collectors.toList());
    }

    public ShopDTO create(@Valid ShopDTO dto) {
        Shop shop = modelMapper.map(dto, Shop.class);
        return modelMapper.map(shopRepository.save(shop), ShopDTO.class);
    }

    public ShopDTO update(Long id, @Valid ShopDTO dto) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Магазин с ID " + id + " не найден"));

        shop.setId(id);
        shop.setName(dto.getName());
        shop.setAdress(dto.getAdress());

        log.info("Shop with id " + shop.getId() + " updated!");
        return modelMapper.map(shopRepository.save(shop), ShopDTO.class);
    }
    public ResponseEntity<ShopDTO> getById(Long id) {
        return ResponseEntity.ok(modelMapper.map(
                shopRepository.findById(id)
                        .orElseThrow(() -> new ServiceException("Магазин с ID " + id + " не найден")),
                ShopDTO.class
        ));
    }
    public ResponseEntity<String> importFromCsv(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The file cannot be empty.");
        }
        List<ShopDTO> shops = new ArrayList<>();

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
                    ShopDTO shopDTO = ShopDTO.builder()
                            .name(nextLine[1])
                            .adress(nextLine[2])
                            .build();
                    shops.add(shopDTO);
                }catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Error in the data format: " + e.getMessage());
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error in data processing: " + e.getMessage());
                }
            }
            for (ShopDTO e : shops) {
                create(e);
            }

            return ResponseEntity.ok("Данные успешно импортированы!");

        } catch (IOException | CsvException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("\n" +
                    "Error processing the file: " + e.getMessage());
        }
    }

}
