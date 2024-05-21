package net.javaguides.springboot.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeEach
	void setUp() {
		employeeRepository.deleteAll(); // For each junit test we need to have clean setup, so using deleteall method.
	}

	
	@Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() 
    		throws Exception{

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("vikas")
                .lastName("kumar")
                .email("vikas@gmail.com")
                .build();

        //In case of Junit we do not require to use Mockito.
        
        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(employee)));

        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",      // $ -> array of JSON Object
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));

    }
	
	//Junit for getAllEmployees
		@Test
		public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList()throws Exception {
			
			//given -precondition or setup
			List<Employee> listOfEmployees = new ArrayList<>();
			listOfEmployees.add(Employee.builder().firstName("Vikas").lastName("kumar").email("vikas@gmail.com").build());
			listOfEmployees.add(Employee.builder().firstName("Ramesh").lastName("Fadatare").email("ramesh@gmail.com").build());
            employeeRepository.saveAll(listOfEmployees);
			
			//when - action or behaviour that we are going to test
			ResultActions response = mockMvc.perform(get("/api/employees"));
			
			//then - verify the output
			response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
		}
		
		//positive scenario - valid employeeId
		//Junit for getEmployeeById
		@Test
		public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception{
			
			//given -precondition or setup
			Employee employee = Employee.builder()
	                .firstName("vikas")
	                .lastName("kumar")
	                .email("vikas@gmail.com")
	                .build();
			employeeRepository.save(employee);
			
			//when - action or behaviour that we are going to test
			ResultActions response =  mockMvc.perform(get("/api/employees/{id}", employee.getId()));
			
			//then - verify the output
			response.andExpect(status().isOk())
			        .andDo(print())
			        .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
			        .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
			        .andExpect(jsonPath("$.email", is(employee.getEmail())));
			
		}
		
		  //negative scenario - valid employeeId
		  //Junit for getEmployeeById
				@Test
				public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception{
					
					//given -precondition or setup
					long employeeId = 1L;
					Employee employee = Employee.builder()
			                .firstName("vikas")
			                .lastName("kumar")
			                .email("vikas@gmail.com")
			                .build();
					employeeRepository.save(employee);
					//when - action or behaviour that we are going to test
					ResultActions response =  mockMvc.perform(get("/api/employees/{id}", employeeId));
					
					//then - verify the output
					response.andExpect(status().isNotFound())
					        .andDo(print());
					
				}
				
				//Junit test for update employee -Positive scenario
				@Test
				public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception{
					
					//given -precondition or setup
					long employeeId = 1L;
					Employee savedEmployee = Employee.builder()
			                .firstName("vikas")
			                .lastName("kumar")
			                .email("vikas@gmail.com")
			                .build();
					employeeRepository.save(savedEmployee);
					
					Employee updateEmployee = Employee.builder()
			                .firstName("Ramesh")
			                .lastName("Fadatare")
			                .email("ramesh@gmail.com")
			                .build();
					
					//when - action or behaviour that we are going to test
					ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
				            .contentType(MediaType.APPLICATION_JSON)
				            .content(objectMapper.writeValueAsString(updateEmployee)));
					
					//then - verify the output
					 response.andExpect(status().isOk())
					 .andDo(print())
		             .andExpect(jsonPath("$.firstName",      // $ -> array of JSON Object
		                     is(updateEmployee.getFirstName())))
		             .andExpect(jsonPath("$.lastName",
		                     is(updateEmployee.getLastName())))
		             .andExpect(jsonPath("$.email",
		                     is(updateEmployee.getEmail())));
					
				}
		
				//Junit test for update employee -Negative scenario
				@Test
				public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception{
					
					//given -precondition or setup
					long employeeId = 1L;
					Employee savedEmployee = Employee.builder()
			                .firstName("vikas")
			                .lastName("kumar")
			                .email("vikas@gmail.com")
			                .build();
					employeeRepository.save(savedEmployee);
					
					Employee updateEmployee = Employee.builder()
			                .firstName("Ramesh")
			                .lastName("Fadatare")
			                .email("ramesh@gmail.com")
			                .build();
					
					//when - action or behaviour that we are going to test
					ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
				            .contentType(MediaType.APPLICATION_JSON)
				            .content(objectMapper.writeValueAsString(updateEmployee)));
					
					//then - verify the output
					 response.andExpect(status().isNotFound())
					 .andDo(print());
					
				}
				
			
				//Junit test for Delete employee
				@Test
				public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
					
					//given -precondition or setup
					Employee savedEmployee = Employee.builder()
			                .firstName("vikas")
			                .lastName("kumar")
			                .email("vikas@gmail.com")
			                .build();
					employeeRepository.save(savedEmployee);
					
					//when - action or behaviour that we are going to test
					ResultActions response =  mockMvc.perform(delete("/api/employee/{id}", savedEmployee.getId()));
					
					//then - verify the output
					response.andExpect(status().isOk()).andDo(print());
					
				}
				
}
