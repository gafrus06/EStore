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
@Table(name = "shops")
public class Shop implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Идентификатор магазина
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shop_seq")
    @SequenceGenerator(name = "shop_seq", sequenceName = "shop_sequence", allocationSize = 1)
    @Column(name = "id_", unique = true, nullable = false)
    Long id;
    /**
     *  Название магазина
     */
    @NotNull(message = "Shop name must not be null")
    @Column(name = "shop_name", nullable = false, length = 250)
    String name;
    /**
     * Адрес магазина
     */
    @NotNull(message = "Shop adress must not be null")
    @Column(name = "adress", nullable = false)
    String adress;

    @OneToMany(mappedBy = "shopId")
    List<ElectroShop> electroShops;

    @OneToMany(mappedBy = "shopId")
    List<Employee> employeeList;


}
