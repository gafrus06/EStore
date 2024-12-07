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
import ru.isands.test.estore.dao.dto.PurchaseTypeDTO;
import ru.isands.test.estore.dao.entity.PurchaseType;
import ru.isands.test.estore.dao.repo.PurchaseTypeRepository;
import ru.isands.test.estore.exception.ServiceException;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PurchaseTypeService{
    private final PurchaseTypeRepository purchaseTypeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PurchaseTypeService(PurchaseTypeRepository purchaseTypeRepository,
                               ModelMapper modelMapper) {
        this.purchaseTypeRepository = purchaseTypeRepository;
        this.modelMapper = modelMapper;
    }


    public List<PurchaseTypeDTO> getAll(){
        return purchaseTypeRepository.findAll()
                .stream()
                .map(purchaseType -> modelMapper.map(purchaseType, PurchaseTypeDTO.class))
                .collect(Collectors.toList());
    }

    public PurchaseTypeDTO create(@Valid PurchaseTypeDTO dto) {
        PurchaseType purchaseType = modelMapper.map(dto, PurchaseType.class);
        return modelMapper.map(purchaseTypeRepository.save(purchaseType), PurchaseTypeDTO.class);
    }

    public PurchaseTypeDTO update(Long id, @Valid PurchaseTypeDTO dto) {
        PurchaseType purchaseType = purchaseTypeRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Тип покупки с ID " + id + " не найден"));

        purchaseType.setId(id);
        purchaseType.setName(dto.getName());
        log.info("Shop with id " + purchaseType.getId() + " updated!");
        return modelMapper.map(purchaseTypeRepository.save(purchaseType), PurchaseTypeDTO.class);
    }
    public ResponseEntity<String> importFromCsv(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The file cannot be empty.");
        }
        List<PurchaseTypeDTO> purchaseTypes = new ArrayList<>();

        try (CSVReader csvReader = new CSVReaderBuilder(
                new InputStreamReader(file.getInputStream(), "windows-1251"))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            String[] nextLine;
            csvReader.readNext();
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length < 2) {
                    continue;
                }
                try {
                    PurchaseTypeDTO purchaseType = PurchaseTypeDTO.builder()
                            .name(nextLine[1])
                            .build();
                    purchaseTypes.add(purchaseType);
                }catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Error in the data format: " + e.getMessage());
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error in data processing: " + e.getMessage());
                }
            }
            for (PurchaseTypeDTO e : purchaseTypes) {
                create(e);
            }

            return ResponseEntity.ok("Данные успешно импортированы!");

        } catch (IOException | CsvException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("\n" +
                    "Error processing the file: " + e.getMessage());
        }
    }

    public ResponseEntity<PurchaseTypeDTO> getById(Long id) {
        return ResponseEntity.ok(modelMapper.map(purchaseTypeRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Тип покупки с ID " + id + " не найден")), PurchaseTypeDTO.class));

    }
}