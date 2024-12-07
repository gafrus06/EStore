package ru.isands.test.estore.dao.repo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.isands.test.estore.dao.dto.EmployeeDTO;
import ru.isands.test.estore.dao.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
	@Query("select e from Employee e where e.lastName = ?1 and e.firstName = ?2 and e.patronymic = ?3 and e.birthDate = ?4")
	Employee findFullName(String lastName, String firstName, String patronymic, Date birthDate);

	@Query("select e from Employee e where e.positionId = ?1")
	List<Employee> findByPosition(Long positionId);
	@Query("SELECT e " +
			"FROM Employee e " +
			"JOIN e.purchases p " +
			"WHERE p.purchaseDate >= :lastYear " +
			"GROUP BY e.id " +
			"ORDER BY COUNT(p.id) DESC")
	List<Employee> findBestEmployeesBySales(@Param("lastYear") LocalDateTime lastYear);

	@Query("SELECT e " +
			"FROM Employee e " +
			"JOIN e.purchases p " +
			"JOIN p.electroItem el " +
			"WHERE p.purchaseDate >= :lastYear " +
			"GROUP BY e.id " +
			"ORDER BY SUM(el.price) DESC")
	List<Employee> findBestEmployeesBySumItems(@Param("lastYear") LocalDateTime lastYear);


	@Query("SELECT e FROM Employee e " +
			"JOIN e.purchases p " +
			"JOIN p.electroItem ei " +
			"WHERE ei.electroType.name= 'Умные часы' " +
			"AND e.positionId.name = 'Младший продавец-консультант' " +
			"GROUP BY e " +
			"ORDER BY COUNT(p) DESC")
	Employee findByBestJunior();

}