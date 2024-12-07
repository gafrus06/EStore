package ru.isands.test.estore.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.isands.test.estore.dao.entity.ElectroType;

@Repository
public interface ElectroTypeRepository extends JpaRepository<ElectroType, Long> {

}
