package ru.isands.test.estore.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "electro_types")
public class ElectroType implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Идентификатор типа электроники
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "electro_type_seq")
    @SequenceGenerator(name = "electro_type_seq", sequenceName = "electro_type_sequence", allocationSize = 1)
    @Column(name = "id_", unique = true, nullable = false)
    Long id;
    /**
     * Название типа электроники
     */
    @NotNull(message = "ElectroType name must not be null")
    @Column(name = "electro_type_name", nullable = false, length = 150)
    String name;


    @OneToMany(mappedBy = "electroType")
    List<ElectroItem> electroItemList;
    @OneToMany(mappedBy = "electroTypeId")
    List<ElectroEmployee> electroEmployeeList;


}
