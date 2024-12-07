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
@Table(name = "electro_items")
public class ElectroItem implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Идентификатор товара
     */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "product_counter")
    @TableGenerator(name = "product_counter", pkColumnName = "name",
            pkColumnValue = "ru.isands.test.estore.dao.entity.ElectroItem",
            table = "counter", valueColumnName = "currentid", allocationSize = 2)
    @Column(name = "id_", unique = true, nullable = false)
    Long id;
    /**
     * Название товара
     */
    @NotNull(message = "ElectroItem name must not be null")
    @Column(name = "name", nullable = false, length = 150)
    String name;
    /**
     * Тип товара
     */
    @NotNull(message = "ElectroItem typeId must not be null")
    @ManyToOne
    @JoinColumn(name = "electroTypeId", nullable = false)
    ElectroType electroType;
    /**
     * Цена товара
     */
    @NotNull(message = "ElectroItem price must not be null")
    @Column(name = "price", nullable = false)
    Double price;
    /**
     * Количество товара
     */
    @NotNull(message = "ElectroItem quantity must not be null")
    @Column(name = "quantity", nullable = false)
     Integer quantity;
    /**
     * Признак архивности товара: true – товара нет в наличии, снят с продаж, false – товар в продаже
     */
    @NotNull(message = "ElectroItem isArchived must not be null")
    @Column(name = "isArchived", nullable = false)
    Boolean isArchived;
    /**
     * Описание товара
     */
    @NotNull(message = "ElectroItem description must not be null")
    @Column(name = "description")
    String description;

    @OneToMany(mappedBy = "electroItemId")
    List<ElectroShop> electroShops;
    @OneToMany(mappedBy = "electroItem")
    List<Purchase> purchaseList;




}
