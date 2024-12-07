package ru.isands.test.estore.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "Employee lastname must not be null")
    private String lastName;

    @NotNull(message = "Employee firstname must not be null")
    private String firstName;

    @NotNull(message = "Employee patronymic must not be null")
    private String patronymic;

    @NotNull(message = "Employee birthDate must not be null")
    private Date birthDate;

    @NotNull(message = "Employee positionId must not be null")
    private Long positionId;

    @NotNull(message = "Employee shopId must not be null")
    private Long shopId;

    @NotNull(message = "Employee gender must not be null")
    private boolean gender;
}
