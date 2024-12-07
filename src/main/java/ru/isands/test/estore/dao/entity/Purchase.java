package ru.isands.test.estore.dao.entity;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "purchase")
public class Purchase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "purchase_counter")
	@TableGenerator(name = "purchase_counter", pkColumnName = "name",
			pkColumnValue = "ru.isands.test.estore.dao.entity.Purchase",
			table = "counter", valueColumnName = "currentid", allocationSize = 1)
	@Column(name = "id_", unique = true, nullable = false)
	Long id;

	@NotNull(message = "Purchase electroId must not be null")
	@ManyToOne
	@JoinColumn(name = "electroId", nullable = false)
	ElectroItem electroItem;

	@NotNull(message = "Purchase employeeId must not be null")
	@ManyToOne
	@JoinColumn(name = "employeeId", nullable = false)
	Employee employee;

	@NotNull(message = "Purchase shopId must not be null")
	@ManyToOne
	@JoinColumn(name = "shopId", nullable = false)
	Shop shop;

	@NotNull(message = "Purchase purchaseDate must not be null")
	@Column(name = "purchaseDate", nullable = false)
	LocalDateTime purchaseDate;

	@NotNull(message = "Purchase type must not be null")
	@ManyToOne
	@JoinColumn(name = "type", nullable = false)
	PurchaseType type;
}