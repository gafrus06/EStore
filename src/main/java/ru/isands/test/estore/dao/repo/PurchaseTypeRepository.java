package ru.isands.test.estore.dao.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.isands.test.estore.dao.entity.PurchaseType;

@Repository
public interface PurchaseTypeRepository extends JpaRepository<PurchaseType, Long>  {
}
