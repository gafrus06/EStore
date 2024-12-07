package ru.isands.test.estore.dao.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.isands.test.estore.dao.entity.ElectroItem;
import ru.isands.test.estore.dao.entity.Employee;
import ru.isands.test.estore.dao.entity.PurchaseType;
import ru.isands.test.estore.dao.entity.Shop;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PurchaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    @NotNull(message = "Purchase electroId must not be null")
    private Long electroItemId;

    @NotNull(message = "Purchase employeeId must not be null")
    private Long employeeId;

    @NotNull(message = "Purchase shopId must not be null")
    private Long shopId;

    @NotNull(message = "Purchase purchaseDate must not be null")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime purchaseDate;

    @NotNull(message = "Purchase type must not be null")
    private Long typeId;
}
