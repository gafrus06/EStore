package ru.isands.test.estore.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.isands.test.estore.dao.entity.ElectroItem;
import ru.isands.test.estore.dao.entity.Shop;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ElectroShopDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long shopId;
    private Long electroItemId;
    @NotNull(message = "ElectroShop count must not be null")
    private int count;
}
