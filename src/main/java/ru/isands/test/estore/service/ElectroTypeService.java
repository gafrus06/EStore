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
import ru.isands.test.estore.dao.dto.ElectroTypeDTO;
import ru.isands.test.estore.dao.entity.ElectroType;
import ru.isands.test.estore.dao.repo.ElectroTypeRepository;
import ru.isands.test.estore.exception.ServiceException;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ElectroTypeService {

    private final ElectroTypeRepository electroTypeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ElectroTypeService(ElectroTypeRepository electroTypeRepository, ModelMapper modelMapper) {
        this.electroTypeRepository = electroTypeRepository;
        this.modelMapper = modelMapper;
    }
    public List<ElectroTypeDTO> getAll(){
        return electroTypeRepository.findAll()
                .stream()
                .map(positionType -> modelMapper.map(positionType, ElectroTypeDTO.class))
                .collect(Collectors.toList());
    }

    public ElectroTypeDTO create(@Valid ElectroTypeDTO dto) {
        ElectroType electroType = modelMapper.map(dto, ElectroType.class);
        log.info(dto.toString());
        return modelMapper.map(electroTypeRepository.save(electroType), ElectroTypeDTO.class);
    }

    public ElectroTypeDTO update(Long id, @Valid ElectroTypeDTO dto) {
        ElectroType electroType = electroTypeRepository.findById(id)
                .orElseThrow(() -> new ServiceException("ElectroType not found"));
        electroType.setName(dto.getName());
        log.info("ElectroType with id " + electroType.getId() + " updated!");
        return modelMapper.map(electroTypeRepository.save(electroType), ElectroTypeDTO.class);
    }
    public ResponseEntity<ElectroTypeDTO> getById(Long id){
        return ResponseEntity.ok(modelMapper.map(electroTypeRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Типа электротовара с ID " + id + " не найдено")),
                ElectroTypeDTO.class));
    }

    public ResponseEntity<String> importFromCsv(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The file cannot be empty.");
        }
        List<ElectroTypeDTO> electroTypes = new ArrayList<>();

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
                    ElectroTypeDTO electroType = ElectroTypeDTO.builder()
                            .name(nextLine[1])
                            .build();
                    electroTypes.add(electroType);
                }catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Error in the data format: " + e.getMessage());
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error in data processing: " + e.getMessage());
                }
            }
            for (ElectroTypeDTO e : electroTypes) {
                create(e);
            }

            return ResponseEntity.ok("Данные успешно импортированы!");

        } catch (IOException | CsvException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("\n" +
                    "Error processing the file: " + e.getMessage());
        }
    }

}
