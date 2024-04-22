package ru.isands.test.estore.dao.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ElectroShopPK implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 *  Идентификатор магазина
	 */
	Long shopId;
	
	/**
	 *  Идентификатор электротовара
	 */
	Long electroItemId;

}
