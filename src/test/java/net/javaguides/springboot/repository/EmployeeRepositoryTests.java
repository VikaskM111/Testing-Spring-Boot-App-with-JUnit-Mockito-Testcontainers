package net.javaguides.springboot.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import net.javaguides.springboot.model.Employee;

@DataJpaTest
public class EmployeeRepositoryTests {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	private Employee employee;
	
	
   @BeforeEach
    public void setUp() {
	   employee = Employee.builder()
			.firstName("Ramesh")
			.lastName("Fadatate")
			.email("ramesh@gmail.com")
			.build();
   }
	

	
	//Junit test for save employee operation;
	@DisplayName("Junit test for save employee operation")
	@Test
	public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
		
		//given -precondition or setup
//		Employee employee = Employee.builder()
//				.firstName("Ramesh")
//				.lastName("Ramesh")
//				.email("ramesh@gmail.com")
//				.build();
		
		//when - action or behaviour that we are going to test
		Employee savedEmployee = employeeRepository.save(employee);

		//then - verify the output
		assertThat(savedEmployee).isNotNull();
		assertThat(savedEmployee.getId()).isGreaterThan(0);
	}
	
	//Junit test for get employee operation.
	@DisplayName("Junit test for get employee operation")
	@Test
	public void givenEmployeeList_whenFindAll_thenEmployeeList() {
		
		//given -precondition or setup
//		Employee employee = Employee.builder()
//				.firstName("Ramesh")
//				.lastName("Ramesh")
//				.email("ramesh@gmail.com")
//				.build();
		Employee employee2 = Employee.builder()
				.firstName("vikas")
				.lastName("mahto")
				.email("vikas@gmail.com")
				.build();
		
		employeeRepository.save(employee);
		employeeRepository.save(employee2);
		
		//when - action or behaviour that we are going to test
		List<Employee> employeeList = employeeRepository.findAll(); 
		
		
		//then - verify the output
		assertThat(employeeList).isNotNull();
		assertThat(employeeList.size()).isEqualTo(2);	
	}
	
	//Junit test for get employee by Id operation.
	@DisplayName("Junit test for get employee by Id operation")
	@Test
	public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
		
		//given -precondition or setup
//		Employee employee = Employee.builder()
//				.firstName("Ramesh")
//				.lastName("Ramesh")
//				.email("ramesh@gmail.com")
//				.build();
		employeeRepository.save(employee);
		
		//when - action or behaviour that we are going to test
		Employee savedEmployee = employeeRepository.findById(employee.getId()).get(); // because findById is Optional
		
		//then - verify the output
		assertThat(savedEmployee).isNotNull();	
	}
	
	//Junit test for get employee by email using custom query operation
	@DisplayName("Junit test for get employee by email operation")
	@Test
	public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {
		
		//given -precondition or setup
//		Employee employee = Employee.builder()
//				.firstName("Ramesh")
//				.lastName("Fadatara")
//				.email("ramesh@gmail.com")
//				.build();
		employeeRepository.save(employee);
		
		//when - action or behaviour that we are going to test
		Employee savedEmployee = employeeRepository.findByEmail(employee.getEmail()).get();
		
		//then - verify the output
		assertThat(savedEmployee).isNotNull();	
	}
	
	//Junit test for update employee operation
	@DisplayName("Junit test for update employee operation")
	@Test
	public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
		
		//given -precondition or setup
//		Employee employee = Employee.builder()
//				.firstName("Ramesh")
//				.lastName("Fadatara")
//				.email("ramesh@gmail.com")
//				.build();
		employeeRepository.save(employee);
		//when - action or behaviour that we are going to test
		Employee savedEmployee =  employeeRepository.findById(employee.getId()).get();
	
		savedEmployee.setFirstName("Ram");
		savedEmployee.setEmail("ram@gamil.com");
		employeeRepository.save(savedEmployee);
		
		//then - verify the output
		assertThat(savedEmployee.getFirstName()).isEqualTo("Ram");
		assertThat(savedEmployee.getEmail()).isEqualTo("ram@gamil.com");
	}
	
	//Junit test for delete employee operation
	@DisplayName("Junit test for delete employee operation")
	@Test
	public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
		
		//given -precondition or setup
//		Employee employee = Employee.builder()
//				.firstName("Ramesh")
//				.lastName("Fadatara")
//				.email("ramesh@gmail.com")
//				.build();
		employeeRepository.save(employee);
		
		//when - action or behaviour that we are going to test
		//employeeRepository.delete(employee);
		
		//delete by Id
		employeeRepository.deleteById(employee.getId());
		Optional<Employee> optionalEmployee = employeeRepository.findById(employee.getId());
		
		
		//then - verify the output
		assertThat(optionalEmployee).isEmpty();
	}
	//Junit test for custom query using JPQL with index
	@DisplayName("Junit test for custom query using JPQL with index")
	@Test
	public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
		
		//given -precondition or setup
		Employee employee = Employee.builder()
				.firstName("Ramesh")
				.lastName("Fadatara")
				.email("ramesh@gmail.com")
				.build();
		employeeRepository.save(employee);
		
		String firstName = "Ramesh";
		String lastName = "Fadatara";
		
		//when - action or behaviour that we are going to test
		Employee savedEmployee = employeeRepository.findByJPQL(firstName, lastName);
		//JPQL -> Java Persistance Query Language
		
		//then - verify the output
		assertThat(savedEmployee).isNotNull();
	}
	
	
	//Junit test for custom query using JPQL with named Params
	@DisplayName("Junit test for custom query using JPQL with named Params")
	@Test
	public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {
		
		//given -precondition or setup
		Employee employee = Employee.builder()
				.firstName("Ramesh")
				.lastName("Fadatara")
				.email("ramesh@gmail.com")
				.build();
		employeeRepository.save(employee);
		
		String firstName = "Ramesh";
		String lastName = "Fadatara";
		
		//when - action or behaviour that we are going to test
		Employee savedEmployee = employeeRepository.findByJPQLNamedParams("Ramesh", "Fadatara");
		//JPQL -> Java Persistance Query Language
		
		//then - verify the output
		assertThat(savedEmployee).isNotNull();
	}
	
	//Junit test for custom query using Native SQL with index params
	@DisplayName("Junit test for custom query using Native SQL with index params")
	@Test
	public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {
		
		//given -precondition or setup
//		Employee employee = Employee.builder()
//				.firstName("Ramesh")
//				.lastName("Fadatara")
//				.email("ramesh@gmail.com")
//				.build();
		employeeRepository.save(employee);
		
//		String firstName = "Ramesh";
//		String lastName = "Fadatara";
		
		//when - action or behaviour that we are going to test
		Employee savedEmployee = employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());
		
		//then - verify the output
		assertThat(savedEmployee).isNotNull();		
	}
	
	//Junit test for custom query using Native SQL with named params
	@DisplayName("Junit test for custom query using Native SQL with named params")
	@Test
	public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {
		
		//given -precondition or setup
//		Employee employee = Employee.builder()
//				.firstName("Ramesh")
//				.lastName("Fadatara")
//				.email("ramesh@gmail.com")
//				.build();
		employeeRepository.save(employee);
		

		
		//when - action or behaviour that we are going to test
		Employee savedEmployee = employeeRepository.findByNativeSQLNamedParams(employee.getFirstName(), employee.getLastName());
		
		//then - verify the output
		assertThat(savedEmployee).isNotNull();		
	}
}
