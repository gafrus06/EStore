package ru.isands.test.estore.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "position_types")
public class PositionType implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Идентификатор должности
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pos_seq")
    @SequenceGenerator(name = "pos_seq", sequenceName = "position_type_sequence", allocationSize = 1)
    @Column(name = "id_", unique = true, nullable = false)
    Long id;
    /**
     * Название должности
     */
    @NotNull(message = "PositionType position_name not be null")
    @Column(name = "position_name", nullable = false, length = 150)
    String name;

    @OneToMany(mappedBy = "positionId")
    List<Employee> employeeList;


}
