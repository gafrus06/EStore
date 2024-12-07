package ru.isands.test.estore.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.isands.test.estore.dao.dto.ElectroEmployeeDTO;
import ru.isands.test.estore.dao.entity.ElectroEmployee;
import ru.isands.test.estore.dao.entity.pk.ElectroEmployeePK;

import java.util.List;

@Repository
public interface ElectroEmployeeRepository extends JpaRepository<ElectroEmployee, ElectroEmployeePK> {

}
