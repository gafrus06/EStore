package ru.isands.test.estore.dao.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;
import ru.isands.test.estore.dao.entity.Employee;
import ru.isands.test.estore.dao.entity.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {




	@Query("SELECT SUM(e.price) AS totalCashSales " +
			"FROM Purchase p " +
			"JOIN p.electroItem e " +
			"WHERE p.type.name = 'Наличные'")
	Double calculateMoneyThroughCash();
	
}
