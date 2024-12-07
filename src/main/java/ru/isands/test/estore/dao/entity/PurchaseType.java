package ru.isands.test.estore.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "purchase_types")
public class PurchaseType {
    private static final long serialVersionUID = 1L;
    /**
     * Идентификатор типа покупки
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchase_type_seq")
    @SequenceGenerator(name = "purchase_type_seq", sequenceName = "purchase_type_sequence", allocationSize = 1)
    @Column(name = "id_", unique = true, nullable = false)
    Long id;
    /**
     * Название типа покупки
     */
    @NotNull(message = "PurchaseType type_name must not be null")
    @Column(name = "purchase_name", nullable = false, length = 150)
    String name;


    @OneToMany(mappedBy = "type")
    List<Purchase> purchaseList;
}
