package net.javaguides.springboot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.willDoNothing;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.impl.EmployeeServiceImpl;

//Here we are using @ExtendWith annotation because we are using @Mock and @InjectMocks annotations.
//Without @ExtendWith(MockitoExtension.class annotation, it will @Mock and @InjectMocks will not work.
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {
	
	@Mock
	private EmployeeRepository employeeRepository;
	
	//@InjectMocks: Inject mock will create a mock object and inject the repository mock to service mock
	@InjectMocks
	private EmployeeServiceImpl employeeService; // we are going to test employee service, and employee service depends on employee repo.

	private Employee employee;
	
	@BeforeEach
	public void setUp() {
	//	employeeRepository = Mockito.mock(EmployeeRepository.class);
	//	employeeService = new EmployeeServiceImpl(employeeRepository);
		employee = Employee.builder()
				.id(1L)
				.firstName("Ramesh")
				.lastName("Fadatate")
				.email("ramesh@gmail.com")
				.build();
	}
	
	
	//Junit test method for save employee
	@DisplayName("Junit test method for save employee")
	@Test
	public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
		
		//given -precondition or setup
		
		given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
		given(employeeRepository.save(employee)).willReturn(employee);
		
		System.out.println(employeeRepository);
		System.out.println(employeeService);
		
		//when - action or behaviour that we are going to test	
		Employee savedEmployee = employeeService.saveEmployee(employee);
		System.out.println(savedEmployee);
		//then - verify the output
		assertThat(savedEmployee).isNotNull();
	}
	
	@DisplayName("Junit test for saveEmployee method which throws Exception")
	@Test
	public void givenExistingEmail_whenSaveEmployee_thenThrowException() {
		
		//given -precondition or setup
		
		given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
		//given(employeeRepository.save(employee)).willReturn(employee);
		
		//when - action or behaviour that we are going to test	
 
		assertThrows(ResourceNotFoundException.class, () -> {
			employeeService.saveEmployee(employee);
		});
		
		//then - verify the output
		verify(employeeRepository, never()).save(any(Employee.class));
	}
	
	//Junit test case for getAllEmployee method
	@DisplayName("Junit test case for getAllEmployee method")
	@Test
	public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeesList() {

		//given -precondition or setup
		Employee employee1 = Employee.builder()
				.id(2L)
				.firstName("Vikas")
				.lastName("mahto")
				.email("vikas@gmail.com")
				.build();
		
		given(employeeRepository.findAll()).willReturn(List.of(employee,employee1));
		
		//when - action or behaviour that we are going to test
		List<Employee> employeeList = employeeService.getAllEmployees();
		
		//then - verify the output
		assertThat(employeeList).isNotNull();
		assertThat(employeeList.size()).isEqualTo(2);
	}
	
	@DisplayName("Junit test case for getAllEmployee method(negative scenario)")
	@Test
	public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEnptyEmployeesList() {

		//given -precondition or setup
		Employee employee1 = Employee.builder()
				.id(2L)
				.firstName("Vikas")
				.lastName("mahto")
				.email("vikas@gmail.com")
				.build();
		
		given(employeeRepository.findAll()).willReturn(Collections.emptyList());
		
		//when - action or behaviour that we are going to test
		List<Employee> employeeList = employeeService.getAllEmployees();
		
		//then - verify the output
		assertThat(employeeList).isEmpty();
		assertThat(employeeList.size()).isEqualTo(0);
	}
	
	//Junit Test for getEmployeeById method
	@DisplayName("Junit Test for getEmployeeById method")
	@Test
	public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
		
		//given -precondition or setup
		given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
		
		//when - action or behaviour that we are going to test
		Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();
		
		//then - verify the output
		assertThat(savedEmployee).isNotNull();
	}
	
	
	//Junit test case updateEmployee method
	@DisplayName("Junit test case updateEmployee method")
	@Test
	public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
		
		//given -precondition or setup
		given(employeeRepository.save(employee)).willReturn(employee);
		employee.setEmail("ram@gmail.com");
		employee.setFirstName("Ram");
		
		//when - action or behaviour that we are going to test
		Employee updatedEmployee = employeeService.updateEmployee(employee);
		
		//then - verify the output
		assertThat(updatedEmployee.getEmail()).isEqualTo("ram@gmail.com");
		assertThat(updatedEmployee.getFirstName()).isEqualTo("Ram");
	}
	
	//Junit test case for delete method
	@DisplayName("Junit test case for delete method")
	@Test
	public void givenEmployeeId_whenDeleteEmployee_thenNothing() {
		
		//given -precondition or setup
		long employeeId = 1L;
		//delete method has void as a return type so willDoNothing is used.
		willDoNothing().given(employeeRepository).deleteById(employeeId);
		
		//when - action or behaviour that we are going to test
		employeeService.deleteEmployee(employeeId);
		
		//then - verify the output
		//since delete method does not return anything, therefore we can only check how many times deleteById method is invoked.
		verify(employeeRepository, times(1)).deleteById(employeeId);
		
		
	}
}
