package ru.isands.test.estore.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import ru.isands.test.estore.dao.entity.ElectroItem;
import ru.isands.test.estore.dao.entity.ElectroShop;
import ru.isands.test.estore.dao.entity.Shop;
import ru.isands.test.estore.dao.entity.pk.ElectroShopPK;

import java.util.List;

@Repository
public interface ElectroShopRepository extends JpaRepository<ElectroShop, ElectroShopPK> {

    List<ElectroShop> findByShopId(Shop shopId);
}
