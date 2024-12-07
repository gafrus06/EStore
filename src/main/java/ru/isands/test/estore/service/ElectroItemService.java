package ru.isands.test.estore.service;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.isands.test.estore.dao.dto.ElectroItemDTO;
import ru.isands.test.estore.dao.entity.ElectroItem;
import ru.isands.test.estore.dao.entity.ElectroType;
import ru.isands.test.estore.dao.repo.ElectroItemRepository;
import ru.isands.test.estore.dao.repo.ElectroTypeRepository;
import ru.isands.test.estore.exception.ServiceException;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ElectroItemService {
    private final ElectroItemRepository electroItemRepository;
    private final ElectroTypeRepository electroTypeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ElectroItemService(ElectroItemRepository electroItemRepository,
                              ElectroTypeRepository electroTypeRepository,
                              ModelMapper modelMapper) {
        this.electroItemRepository = electroItemRepository;
        this.electroTypeRepository = electroTypeRepository;
        this.modelMapper = modelMapper;
    }


    public List<ElectroItemDTO> getAll() {
        return electroItemRepository.findAll()
                .stream()
                .map(electroItem -> modelMapper.map(electroItem, ElectroItemDTO.class))
                .collect(Collectors.toList());
    }

    public ElectroItemDTO create(@Valid ElectroItemDTO entity) {
        ElectroType electroType = electroTypeRepository.findById(entity.getElectroTypeId())
                .orElseThrow(() -> new ServiceException("Тип электротовара с ID " + entity.getElectroTypeId() + " не найден"));
        ElectroItem electroItem = modelMapper.map(entity, ElectroItem.class);
        electroItem.setElectroType(electroType);
        ElectroItem savedElectroItem = electroItemRepository.save(electroItem);
        log.info("ElectroItem with id " + savedElectroItem.getId() + " added!");
        return modelMapper.map(savedElectroItem, ElectroItemDTO.class);
    }
    public ElectroItemDTO update(Long id, @Valid ElectroItemDTO dto) {
        ElectroItem electroItem = electroItemRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Электротовар с ID " + id + " не найден"));
        modelMapper.map(dto, ElectroItem.class);
        ElectroType electroType = electroTypeRepository.findById(dto.getElectroTypeId())
                .orElseThrow(() -> new ServiceException("Тип электротовара с ID " + dto.getElectroTypeId() + " не найден"));
        electroItem.setElectroType(electroType);
        electroItem.setId(id);
        electroItem.setName(dto.getName());
        electroItem.setPrice(dto.getPrice());
        electroItem.setIsArchived(dto.getIsArchived());
        electroItem.setQuantity(dto.getQuantity());
        electroItem.setDescription(dto.getDescription());
        ElectroItem updatedElectroItem = electroItemRepository.save(electroItem);
        log.info("ElectroItem with id " + electroItem.getId() + " updated!");
        return modelMapper.map(updatedElectroItem, ElectroItemDTO.class);
    }
    public ResponseEntity<String> importFromCsv(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("The file cannot be empty.");
        }
        List<ElectroItemDTO> electroItemDTOS = new ArrayList<>();
        try (CSVReader csvReader = new CSVReaderBuilder(
                new InputStreamReader(file.getInputStream(), "windows-1251"))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            String[] nextLine;
            csvReader.readNext(); // Skip the header
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length < 7) {
                    throw new IllegalArgumentException("Incorrect data format in line: " + Arrays.toString(nextLine));
                }
                ElectroItemDTO item = new ElectroItemDTO();
                try {
                    item.setName(nextLine[1]);
                    item.setElectroTypeId(Long.valueOf(nextLine[2]));
                    item.setPrice(Double.valueOf(nextLine[3]));
                    item.setQuantity(Integer.valueOf(nextLine[4]));
                    item.setIsArchived(nextLine[5].equals("1"));
                    item.setDescription(nextLine[6]);
                    electroItemDTOS.add(item);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Data format error: " + e.getMessage());
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error processing data: " + e.getMessage());
                }
            }
            for (ElectroItemDTO item : electroItemDTOS) {
                create(item);
            }
            return ResponseEntity.ok("Data imported successfully!");
        } catch (IOException | CsvException e) {
            throw new RuntimeException("Error processing file: " + e.getMessage());
        }
    }
    public ResponseEntity<ElectroItemDTO> getById(Long id) {
        return ResponseEntity.ok(modelMapper.map(electroItemRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Электротовар с ID " + id + " не найден")), ElectroItemDTO.class));
    }

}
