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
import ru.isands.test.estore.dao.dto.EmployeeDTO;
import ru.isands.test.estore.dao.entity.Employee;
import ru.isands.test.estore.dao.entity.PositionType;
import ru.isands.test.estore.dao.entity.Shop;
import ru.isands.test.estore.dao.repo.EmployeeRepository;
import ru.isands.test.estore.dao.repo.PositionTypeRepository;
import ru.isands.test.estore.dao.repo.ShopRepository;
import ru.isands.test.estore.exception.ServiceException;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PositionTypeRepository positionTypeRepository;
    private final ShopRepository shopRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,
                           PositionTypeRepository positionTypeRepository,
                           ShopRepository shopRepository,
                           ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.positionTypeRepository = positionTypeRepository;
        this.shopRepository = shopRepository;
        this.modelMapper = modelMapper;
    }

    public List<EmployeeDTO> getAllEmployeeDTO() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    public EmployeeDTO create(@Valid EmployeeDTO employeeDTO) {
        Shop shop = shopRepository.findById(employeeDTO.getShopId())
                .orElseThrow(() -> new ServiceException("Магазин с ID " + employeeDTO.getShopId() + " не найден"));

        PositionType positionType = positionTypeRepository.findById(employeeDTO.getPositionId())
                .orElseThrow(() -> new ServiceException("Типа должности с ID " + employeeDTO.getPositionId() + " не найдена"));

        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        employee.setShopId(shop);
        employee.setPositionId(positionType);
        return modelMapper.map(employeeRepository.save(employee), EmployeeDTO.class);
    }
    public EmployeeDTO update(Long id, @Valid EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Сотрудник с ID " + id + " не найден"));
        modelMapper.map(employeeDTO, employee);

        PositionType positionType = positionTypeRepository.findById(employeeDTO.getPositionId())
                .orElseThrow(() -> new ServiceException("Типа должности с ID " + employeeDTO.getPositionId() + " не найдена"));
        Shop shop = shopRepository.findById(employeeDTO.getShopId())
                .orElseThrow(() -> new ServiceException("Магазин с ID " + employeeDTO.getShopId() + " не найден"));
        employee.setId(id);
        employee.setPositionId(positionType);
        employee.setShopId(shop);
        Employee updatedEmployee = employeeRepository.save(employee);
        log.info("Employee with id " + updatedEmployee.getId() + " updated!");

        return modelMapper.map(updatedEmployee, EmployeeDTO.class);
    }


    public ResponseEntity<String> importFromCsv(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The file cannot be empty.");
        }
        List<EmployeeDTO> employees = new ArrayList<>();

        try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream(), "windows-1251"))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            String[] nextLine;
            csvReader.readNext();
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length < 8) {
                    continue;
                }
                try {
                    EmployeeDTO employeeDTO = EmployeeDTO.builder()
                            .lastName(nextLine[1])
                            .firstName(nextLine[2])
                            .patronymic(nextLine[3])
                            .birthDate(parseBirthDate(nextLine[4]))
                            .positionId(Long.parseLong(nextLine[5]))
                            .shopId(Long.parseLong(nextLine[6]))
                            .gender(nextLine[7].equals("1"))
                            .build();
                    employees.add(employeeDTO);
                }catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Error in the data format: " + e.getMessage());
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error in data processing: " + e.getMessage());
                }
            }
            for (EmployeeDTO e : employees) {
                create(e);
            }

            return ResponseEntity.ok("Данные успешно импортированы!");

        } catch (IOException | CsvException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("\n" +
                    "Error processing the file: " + e.getMessage());
        }
    }
    private static Date parseBirthDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            return Date.valueOf(localDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateString, e);
        }
    }
    public EmployeeDTO getBestJunior(){
        Employee e  = employeeRepository.findByBestJunior();
        if(employeeRepository.findByBestJunior() != null){
            return modelMapper.map(e, EmployeeDTO.class);
        }
        throw new ServiceException("Лучший младший продавец-консультант не найден");

    }


    public EmployeeDTO getById(Long id) {
        return modelMapper.map(employeeRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Сотрудник с ID " + id + " не найден")), EmployeeDTO.class);
    }
    public List<EmployeeDTO> getBestEmployeesBySales(){

        return employeeRepository.findBestEmployeesBySales(LocalDateTime.now().minusYears(1))
                .stream()
                .map(e -> modelMapper.map(e, EmployeeDTO.class))
                .collect(Collectors.toList());
    }
    public List<EmployeeDTO> getBestEmployeesBySumItems(){
        return employeeRepository.findBestEmployeesBySumItems(LocalDateTime.now().minusYears(1))
                .stream()
                .map(e -> modelMapper.map(e, EmployeeDTO.class))
                .collect(Collectors.toList());
    }
}