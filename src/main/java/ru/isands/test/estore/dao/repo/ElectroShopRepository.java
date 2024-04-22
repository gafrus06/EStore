package ru.isands.test.estore.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.isands.test.estore.dao.entity.ElectroShop;
import ru.isands.test.estore.dao.entity.ElectroShopPK;

public interface ElectroShopRepository extends JpaRepository<ElectroShop, ElectroShopPK> {
	
}
