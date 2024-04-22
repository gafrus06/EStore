package ru.isands.test.estore.dao.entity;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "store_purchase")
public class Purchase implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Идентификатор покупки
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "purchase_counter")
	@TableGenerator(name = "purchase_counter", pkColumnName = "name", pkColumnValue = "ru.isands.test.estore.dao.entity.Purchase", table = "counter", valueColumnName = "currentid", allocationSize = 1)
	@Column(name = "id_", unique = true, nullable = false)
	Long id;
	
	/**
	 * Идентификатор товара
	 */
	@Column(name = "electroId", nullable = false)
	Long electroId;
	
	/**
	 * Идентификатор сотрудника
	 */
	@Column(name = "employeeId", nullable = false)
	Long employeeId;
	
	/**
	 * Идентификатор магазина
	 */
	@Column(name = "shopId", nullable = false)
	Long shopId;
	
	/**
	 * Дата совершения покупки
	 */
	@Column(name = "purchaseDate", nullable = false)
	Date purchaseDate;
	
	/**
	 * Способ оплаты
	 */
	@Column(name = "type", nullable = false)
	int type;
	
}