package com.godeltech.mastery.employeeservice.service;

import com.godeltech.mastery.employeeservice.dao.EmployeeRepository;
import com.godeltech.mastery.employeeservice.dao.entity.Employee;
import com.godeltech.mastery.employeeservice.dao.entity.Gender;
import com.godeltech.mastery.employeeservice.dao.specification.EmployeeSpecification;
import com.godeltech.mastery.employeeservice.dao.specification.Operation;
import com.godeltech.mastery.employeeservice.dao.specification.SearchCriteria;
import com.godeltech.mastery.employeeservice.dao.specification.factory.PredicateFactory;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoRequest;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoResponse;
import com.godeltech.mastery.employeeservice.exception.ResourceNotFoundException;
import com.godeltech.mastery.employeeservice.service.impl.EmployeeServiceImpl;
import lombok.var;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Autowired
    private PredicateFactory<Employee> predicateFactory;

    private final ModelMapper modelMapper2 =new ModelMapper();

    EmployeeDtoResponse expected;
    EmployeeDtoRequest employeeDtoRequest;
    Employee employee;

    @BeforeEach
    public void setUp() {
        employee = Employee.builder()
                .employeeId(1L)
                .firstName("Artem")
                .lastName("Myachin")
                .departmentId(1L)
                .jobTittle("IT")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.now())
                .build();
        employeeDtoRequest = modelMapper2.map(employee, EmployeeDtoRequest.class);
        expected=modelMapper2.map(employee,EmployeeDtoResponse.class);
    }

    @AfterEach
    public void tearDown(){
        expected =null;
        employeeDtoRequest =null;
        employee=null;
    }

    @Test
    void getEmployees() {
        List<EmployeeDtoResponse> expected = Collections.singletonList(this.expected);
        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(employee));
        when(modelMapper.map(employee,EmployeeDtoResponse.class)).thenReturn(this.expected);

        var actual = employeeService.getEmployees();

        assertEquals(expected, actual);
        verify(employeeRepository).findAll();
    }

    @Test
    void getEmployeeById() throws ResourceNotFoundException {
        when(employeeRepository.findById(1L)).thenReturn(Optional.ofNullable(employee));
        when(modelMapper.map(employee,EmployeeDtoResponse.class)).thenReturn(expected);

        var actual = employeeService.getEmployeeById(1L);

        assertEquals(actual, expected);
        verify(employeeRepository).findById(1L);
    }

    @Test
    void getEmployeeByIdFail() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        var actual = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.getEmployeeById(1L));

        assertTrue(actual.getMessage().contains("Employee wasn't found by employeeId=1"));
        verify(employeeRepository).findById(1L);
    }

    @Test
    void update() throws ResourceNotFoundException {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(modelMapper.map(employeeDtoRequest,Employee.class)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(modelMapper.map(employee,EmployeeDtoResponse.class)).thenReturn(expected);

        var actual = employeeService.update(1L, employeeDtoRequest);

        assertEquals(actual, expected);
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(employee);
    }

    @Test
    void updateFail() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        var actual = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.update(1L, employeeDtoRequest));

        assertTrue(actual.getMessage().contains("Employee wasn't found by employeeId=1"));
        verify(employeeRepository).findById(1L);
        verify(employeeRepository, never()).save(employee);
    }

    @Test
    void deleteById() throws ResourceNotFoundException {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(modelMapper.map(employee,EmployeeDtoResponse.class)).thenReturn(expected);

        employeeService.deleteById(1L);

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void deleteByIdFail() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        var actual = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.deleteById(1L));

        assertTrue(actual.getMessage().contains("Employee wasn't found by employeeId=1"));
        verify(employeeRepository).findById(1L);
        verify(employeeRepository,never()).deleteById(1L);
    }

    @Test
    void findByFirstName() throws ResourceNotFoundException {
        List<EmployeeDtoResponse> expectedList = Collections.singletonList(expected);
        when(employeeRepository.findAll(new EmployeeSpecification(
                new SearchCriteria("firstName", Operation.EQUALS,expected.getFirstName()),predicateFactory))
        ).thenReturn(Collections.singletonList(employee));
        when(modelMapper.map(employee,EmployeeDtoResponse.class)).thenReturn(expected);

        var actual = employeeService.findByFirstNameLike(this.expected.getFirstName());

        assertEquals(actual, expectedList);
        verify(employeeRepository).findAll(new EmployeeSpecification(
                new SearchCriteria("firstName",Operation.EQUALS,expected.getFirstName()),predicateFactory));
    }

}