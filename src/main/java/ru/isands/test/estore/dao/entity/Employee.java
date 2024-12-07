package ru.isands.test.estore.dao.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * Идентификатор сотрудника
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "employee_counter")
	@TableGenerator(name = "employee_counter", pkColumnName = "name",
			pkColumnValue = "ru.isands.test.estore.dao.entity.Employee",
			table = "counter", valueColumnName = "currentid", allocationSize = 2)
	@Column(name = "id_", unique = true, nullable = false)
	Long id;
	/**
	 * Фамилия сотрудника
	 */
	@NotNull(message = "Employee lastname must not be null")
	@Column(name = "lastname", nullable = false, length = 100)
	String lastName;
	/**
	 * Имя сотрудника
	 */
	@NotNull(message = "Employee firstname must not be null")
	@Column(name = "firstname", nullable = false, length = 100)
	String firstName;
	/**
	 * Отчество сотрудника
	 */
	@NotNull(message = "Employee patronymic must not be null")
	@Column(name = "patronymic", nullable = false, length = 100)
	String patronymic;
	/**
	 * Дата рождения
	 */
	@NotNull(message = "Employee birthDate must not be null")
	@Column(name = "birthDate", nullable = false)
	Date birthDate;
	/**
	 * Должность сотрудника
	 */
	@NotNull(message = "Employee positionId must not be null")
	@ManyToOne
	@JoinColumn(name = "positionId", nullable = false)
	PositionType positionId;
	/**
	 * Ссылка на магазиг
	 */
	@NotNull(message = "Employee shopId must not be null")
	@ManyToOne
	@JoinColumn(name = "shopId", nullable = false)
	Shop shopId;
	/**
	 * Пол сотрудника
	 */
	@NotNull(message = "Employee gender must not be null")
	@Column(name = "gender", nullable = false)
	boolean gender;

	@OneToMany(mappedBy = "employee")
	List<Purchase> purchases;

	@OneToMany(mappedBy = "employeeId")
	List<ElectroEmployee> electroEmployeeList;



}
