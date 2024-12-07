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
import ru.isands.test.estore.dao.dto.PositionTypeDTO;
import ru.isands.test.estore.dao.dto.PurchaseTypeDTO;
import ru.isands.test.estore.dao.dto.ShopDTO;
import ru.isands.test.estore.dao.entity.PositionType;
import ru.isands.test.estore.dao.entity.PurchaseType;
import ru.isands.test.estore.dao.repo.PositionTypeRepository;
import ru.isands.test.estore.exception.ServiceException;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PositionTypeService {
    private final PositionTypeRepository positionTypeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PositionTypeService(PositionTypeRepository positionTypeRepository, ModelMapper modelMapper) {
        this.positionTypeRepository = positionTypeRepository;
        this.modelMapper = modelMapper;
    }

    public List<PositionTypeDTO> getAll(){
        return positionTypeRepository.findAll()
                .stream()
                .map(positionType -> modelMapper.map(positionType, PositionTypeDTO.class))
                .collect(Collectors.toList());
    }

    public PositionTypeDTO create(@Valid PositionTypeDTO dto) {
        PositionType positionType = modelMapper.map(dto, PositionType.class);
        return modelMapper.map(positionTypeRepository.save(positionType), PositionTypeDTO.class);
    }

    public PositionTypeDTO update(Long id, @Valid PositionTypeDTO dto) {
        PositionType positionType = positionTypeRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Типа должности с ID " + id + " не найдена"));

        positionType.setId(id);
        positionType.setName(dto.getName());
        log.info("PositionType with id " + positionType.getId() + " updated!");
        return modelMapper.map(positionTypeRepository.save(positionType), PositionTypeDTO.class);
    }


    public ResponseEntity<PositionTypeDTO> getById(Long id) {
        return ResponseEntity.ok(modelMapper.map(
                positionTypeRepository.findById(id)
                        .orElseThrow(() -> new ServiceException("Типа должности с ID " + id + " не найдена")),
                PositionTypeDTO.class
        ));
    }
    public ResponseEntity<String> importFromCsv(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The file cannot be empty.");
        }
        List<PositionTypeDTO> positionTypes = new ArrayList<>();

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
                    PositionTypeDTO positionType = PositionTypeDTO.builder()
                            .name(nextLine[1])
                            .build();
                    positionTypes.add(positionType);
                }catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Error in the data format: " + e.getMessage());
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error in data processing: " + e.getMessage());
                }
            }
            for (PositionTypeDTO e : positionTypes) {
                create(e);
            }

            return ResponseEntity.ok("Данные успешно импортированы!");

        } catch (IOException | CsvException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("\n" +
                    "Error processing the file: " + e.getMessage());
        }
    }
}
