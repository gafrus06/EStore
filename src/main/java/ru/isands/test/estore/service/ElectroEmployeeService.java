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
import ru.isands.test.estore.dao.dto.ElectroEmployeeDTO;
import ru.isands.test.estore.dao.entity.ElectroEmployee;
import ru.isands.test.estore.dao.entity.ElectroType;
import ru.isands.test.estore.dao.entity.Employee;
import ru.isands.test.estore.dao.entity.pk.ElectroEmployeePK;
import ru.isands.test.estore.dao.repo.ElectroEmployeeRepository;
import ru.isands.test.estore.dao.repo.ElectroTypeRepository;
import ru.isands.test.estore.dao.repo.EmployeeRepository;
import ru.isands.test.estore.exception.ServiceException;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ElectroEmployeeService {

    private final ElectroEmployeeRepository electroEmployeeRepository;
    private final ElectroTypeRepository electroTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ElectroEmployeeService(ElectroEmployeeRepository electroEmployeeRepository,
                                  ElectroTypeRepository electroTypeRepository,
                                  EmployeeRepository employeeRepository,
                                  ModelMapper modelMapper) {
        this.electroEmployeeRepository = electroEmployeeRepository;
        this.electroTypeRepository = electroTypeRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }
    public ElectroEmployeeDTO create(@Valid ElectroEmployeeDTO dto) {
        ElectroEmployee eEmp = modelMapper.map(dto, ElectroEmployee.class);
        return modelMapper.map(electroEmployeeRepository.save(eEmp), ElectroEmployeeDTO.class);
    }
    public ElectroEmployeeDTO update(Long employeeId, Long electroTypeId, ElectroEmployeeDTO dto) {
        ElectroEmployee electroEmployee = electroEmployeeRepository.findById(new ElectroEmployeePK(employeeId, electroTypeId))
                .orElseThrow(() -> new ServiceException("ElectroShop not found"));
        modelMapper.map(dto, electroEmployee);
        ElectroType electroType = electroTypeRepository.findById(electroTypeId)
                .orElseThrow(() -> new ServiceException("Тип электротовара с ID " + electroTypeId + " не найден"));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ServiceException("Сотрудник с ID " + employeeId + " не найден"));

        electroEmployee.setElectroTypeId(electroType);
        electroEmployee.setEmployeeId(employee);
        ElectroEmployee updated= electroEmployeeRepository.save(electroEmployee);
        log.info("Employee with id " + updated.getEmployeeId().getId() + " " + updated.getElectroTypeId().getId() + " updated!");
        return modelMapper.map(updated, ElectroEmployeeDTO.class);
    }
    public List<ElectroEmployeeDTO> getAll(){
        return electroEmployeeRepository.findAll()
                .stream()
                .map(e -> modelMapper.map(e, ElectroEmployeeDTO.class))
                .collect(Collectors.toList());
    }
    public ResponseEntity<String> importFromCsv(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The file cannot be empty.");
        }
        List<ElectroEmployeeDTO> electroEmployees = new ArrayList<>();

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
                    ElectroEmployeeDTO electroEmployee = ElectroEmployeeDTO.builder()
                            .employeeId(Long.parseLong(nextLine[0]))
                            .electroTypeId(Long.parseLong(nextLine[1]))
                            .build();
                    electroEmployees.add(electroEmployee);
                }catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Error in the data format: " + e.getMessage());
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error in data processing: " + e.getMessage());
                }
            }
            for (ElectroEmployeeDTO e : electroEmployees) {
                create(e);
            }

            return ResponseEntity.ok("Данные успешно импортированы!");

        } catch (IOException | CsvException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("\n" +
                    "Error processing the file: " + e.getMessage());
        }
    }
}
