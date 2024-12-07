package ru.isands.test.estore.dao.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import ru.isands.test.estore.dao.entity.pk.ElectroShopPK;

@Getter
@Setter
@Entity
@IdClass(ElectroShopPK.class)
@Table(name = "electro_shop")
public class ElectroShop {

	/**
	 * Идентификатор магазина
	 */
	@Id
	@ManyToOne
	@JoinColumn(name = "shopId", referencedColumnName = "id_", nullable = false)
	private Shop shopId;
	/**
	 * Идентификатор товара
	 */
	@Id
	@ManyToOne
	@JoinColumn(name = "electroItemId", referencedColumnName = "id_", nullable = false)
	private ElectroItem electroItemId;

	/**
	 * Оставшееся количество товара в магазине
	 */
	@Column(name = "count_", nullable = false)
	@NotNull(message = "ElectroShop count must not be null")
	private int count;
}

