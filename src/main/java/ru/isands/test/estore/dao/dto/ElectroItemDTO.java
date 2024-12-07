package ru.isands.test.estore.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.isands.test.estore.dao.entity.ElectroType;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ElectroItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "ElectroItem name must not be null")
    private String name;

    @NotNull(message = "ElectroItem electroTypeId must not be null")
    private Long electroTypeId;
    @NotNull(message = "ElectroItem price must not be null")
    private Double price;

    @NotNull(message = "ElectroItem quantity must not be null")
    private Integer quantity;

    @NotNull(message = "ElectroItem isArchived must not be null")
    private Boolean isArchived;

    @NotNull(message = "ElectroItem description must not be null")
    private String description;
}
