package ru.isands.test.estore.dao.entity.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElectroEmployeePK implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *  Идентификатор сотрудника
     */
    Long employeeId;

    /**
     * Идентификатор типа электроники
      */
    Long electroTypeId;
}
