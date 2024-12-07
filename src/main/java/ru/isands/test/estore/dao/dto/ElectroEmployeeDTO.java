package ru.isands.test.estore.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ElectroEmployeeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long employeeId;
    private Long electroTypeId;
}
