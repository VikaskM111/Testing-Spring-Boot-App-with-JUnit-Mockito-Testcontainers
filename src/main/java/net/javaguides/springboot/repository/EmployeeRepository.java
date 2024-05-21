package net.javaguides.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.javaguides.springboot.model.Employee;

//By default all the methods in JpaRepository is transactional
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	
	//Custom query using method name approach
	Optional<Employee> findByEmail(String email);
	
	//define custom query using JPQL with index params
	@Query("Select e from Employee e where e.firstName =?1 and e.lastName = ?2") // Here Employee is the class name not table name.
	Employee findByJPQL(String firstName, String lastName);
	
	//define custom query using JPQL with named params
	@Query("Select e from Employee e where e.firstName = :firstName and e.lastName = :lastName") 
	Employee findByJPQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

	//define custom query using Native SQL with index params
	@Query(value = "select * from employees e where e.first_Name  =?1 and e.last_Name = ?2", nativeQuery = true) // Here Employees is the table
	Employee findByNativeSQL(String firstName, String lastName);
	
	//define custom query using Native SQL with named params
	@Query(value = "select * from employees e where e.first_Name  =:firstName and e.last_Name = :lastName", nativeQuery = true) // Here Employees is the table
	Employee findByNativeSQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
