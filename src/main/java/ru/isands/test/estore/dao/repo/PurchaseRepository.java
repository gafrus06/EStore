package ru.isands.test.estore.dao.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.isands.test.estore.dao.entity.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
	
	@Query("select p from Purchase p where p.shopId = :shopId")
	public List<Purchase> findByShop(@Param("shopId") Long shopId);
	
}
