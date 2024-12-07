package ru.isands.test.estore.dao.entity;

import lombok.Getter;
import lombok.Setter;
import ru.isands.test.estore.dao.entity.pk.ElectroEmployeePK;

import javax.persistence.*;

@Setter
@Getter
@Entity
@IdClass(ElectroEmployeePK.class)
@Table(name = "electro_employee")
public class ElectroEmployee {
    /**
     * Идентификатор сотрудника
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "employeeId", referencedColumnName = "id_", nullable = false)
    private Employee employeeId;
    /**
     * Идентификатор типа электроники
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "electroTypeId", referencedColumnName = "id_", nullable = false)
    private ElectroType electroTypeId;

}
