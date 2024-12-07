package ru.isands.test.estore.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    @NotNull(message = "Shop name must not be null")
    private String name;
    @NotNull(message = "Shop adress must not be null")
    private String adress;
}
