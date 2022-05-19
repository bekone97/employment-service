package com.godeltech.mastery.employeeservice.service;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.godeltech.mastery.employeeservice.clients.DepartmentApiClient;
import com.godeltech.mastery.employeeservice.dao.entity.Employee;
import com.godeltech.mastery.employeeservice.dao.entity.Gender;
import com.godeltech.mastery.employeeservice.dao.entity.Phone;
import com.godeltech.mastery.employeeservice.dataTypeFactory.AdditionalPostgresDataTypeFactory;
import com.godeltech.mastery.employeeservice.dto.DepartmentDtoResponse;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoRequest;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoResponse;
import com.godeltech.mastery.employeeservice.dto.PhoneDto;
import com.godeltech.mastery.employeeservice.exception.ResourceNotFoundException;
import com.godeltech.mastery.employeeservice.initializer.DatabaseContainerInitializer;
import com.godeltech.mastery.employeeservice.mapping.EmployeeMapper;
import lombok.var;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE,
        dataTypeFactoryClass = AdditionalPostgresDataTypeFactory.class)
@ActiveProfiles("test")
class EmployeeServiceIntegrationTest extends DatabaseContainerInitializer {
    @Autowired
    private EmployeeService employeeService;
    @MockBean
    private DepartmentApiClient departmentApiClient;

    @Autowired
    private EmployeeMapper employeeMapper;
    @MockBean
    private KafkaMessageSender kafkaMessageSender;
    private final ModelMapper modelMapper2 =new ModelMapper();

    EmployeeDtoResponse expected;
    EmployeeDtoRequest employeeDtoRequest;
    Employee employee;
    DepartmentDtoResponse departmentDtoResponse;
    PhoneDto phoneDto;

    @BeforeEach
    public void setUp() {
        List<Phone> phones = new ArrayList<>();
        phones.add(Phone.builder()
                        .phoneId(1L)
                .number(297342979)
                .employee(employee)
                .build());
        employee = Employee.builder()
                .employeeId(1L)
                .firstName("Artem")
                .lastName("Myachin")
                .departmentId(1L)
                .jobTittle("sales")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(1997,06,25))
                .phones(phones)
                .build();
        departmentDtoResponse=DepartmentDtoResponse.builder()
                .departmentId(1L)
                .departmentName("It")
                .build();
        employeeDtoRequest = modelMapper2.map(employee, EmployeeDtoRequest.class);
        expected=employeeMapper.mapToEmployeeDtoResponse(employee,departmentDtoResponse);
    }

    @AfterEach
    public void tearDown(){
        expected =null;
        employeeDtoRequest =null;
        employee=null;
        phoneDto=null;
    }

    @Test
    @DataSet(value = {"dataset/init/employee/init.yml", "dataset/init/phone/init.yml"},useSequenceFiltering = false,cleanBefore = true)
    void getEmployees() {
        when(departmentApiClient.getDepartmentDtoById(1L)).thenReturn(departmentDtoResponse);
        var actual = employeeService.getEmployees();

        assertEquals(1,actual.size());
        assertEquals(expected, actual.get(0));

        verify(departmentApiClient,times(1)).getDepartmentDtoById(departmentDtoResponse.getDepartmentId());
    }

    @Test
    @DataSet(value = {"dataset/init/employee/init.yml", "dataset/init/phone/init.yml"},useSequenceFiltering = false,cleanBefore = true)
    void getEmployeeById() throws ResourceNotFoundException {
        when(departmentApiClient.getDepartmentDtoById(1L)).thenReturn(departmentDtoResponse);

        var actual = employeeService.getEmployeeById(1L);

        assertEquals(expected,actual);
        verify(departmentApiClient).getDepartmentDtoById(departmentDtoResponse.getDepartmentId());
    }

    @Test
    @DataSet(value = {"dataset/init/employee/init.yml", "dataset/init/phone/init.yml"},useSequenceFiltering = false,cleanBefore = true)
    void getEmployeeByIdFail() {
        var actual = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.getEmployeeById(2L));

        assertTrue(actual.getMessage().contains("Employee wasn't found by employeeId=2"));

        verify(departmentApiClient,never()).getDepartmentDtoById(departmentDtoResponse.getDepartmentId());
    }


    @Test
    @DataSet(value = {"dataset/init/employee/init.yml", "dataset/init/phone/init.yml"},useSequenceFiltering = false,cleanBefore = true)
    @ExpectedDataSet(value = {"dataset/expected/employee/update.yml","dataset/expected/phone/updateEmployee.yml"})
    void update() throws ResourceNotFoundException {
        employeeDtoRequest.setGender(Gender.FEMALE);
        expected.setGender(Gender.FEMALE);
        when(departmentApiClient.getDepartmentDtoById(1L)).thenReturn(departmentDtoResponse);

        var actual = employeeService.update(1L, employeeDtoRequest);

        assertEquals(actual, expected);

        verify(departmentApiClient).getDepartmentDtoById(1L);
    }

    @Test
    @DataSet(value = {"dataset/init/employee/init.yml","dataset/init/phone/init.yml"},useSequenceFiltering = false,cleanBefore = true)
    void updateFail() {

        var actual = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.update(2L, employeeDtoRequest));

        assertTrue(actual.getMessage().contains("Employee wasn't found by employeeId=2"));

    }

    @Test
    @DataSet(value = {"dataset/init/employee/init.yml", "dataset/init/phone/init.yml"},useSequenceFiltering = false,cleanBefore = true)
    @ExpectedDataSet(value = {"dataset/expected/employee/delete.yml", "dataset/expected/phone/delete.yml"})
    void deleteById() throws ResourceNotFoundException {
        when(departmentApiClient.getDepartmentDtoById(1L)).thenReturn(departmentDtoResponse);

        employeeService.deleteById(1L);

        verify(departmentApiClient,times(1)).getDepartmentDtoById(1L);
    }

    @Test
    @DataSet(value = {"dataset/init/employee/init.yml", "dataset/init/phone/init.yml"},useSequenceFiltering = false,cleanBefore = true)
    void deleteByIdFail() {

        var actual = assertThrows(ResourceNotFoundException.class,
                () -> employeeService.deleteById(2L));

        assertTrue(actual.getMessage().contains("Employee wasn't found by employeeId=2"));

    }

    @Test
    @DataSet(value = {"dataset/init/employee/init.yml", "dataset/init/phone/init.yml"},useSequenceFiltering = false,cleanBefore = true)
    void findByFirstName() throws ResourceNotFoundException {
        List<EmployeeDtoResponse> expectedList = Collections.singletonList(expected);

        when(departmentApiClient.getDepartmentDtoById(1L)).thenReturn(departmentDtoResponse);

        var actual = employeeService.findByFirstNameLike(this.expected.getFirstName());

        assertEquals( expectedList, actual);

        verify(departmentApiClient).getDepartmentDtoById(1L);
    }
    @Test
    @DataSet(value = {"dataset/init/employee/init.yml", "dataset/init/phone/init.yml"},
            useSequenceFiltering = false,
            executeScriptsBefore = {"scripts/employeeSeq.sql", "scripts/phoneSeq.sql"})
    @ExpectedDataSet(value = {"dataset/expected/employee/save.yml", "dataset/expected/phone/saveEmployee.yml"})
    void save() throws ExecutionException, InterruptedException, TimeoutException {
        phoneDto = PhoneDto.builder()
                .number(287945678)
                .build();
        List<PhoneDto> phones = new ArrayList<>();
        phones.add(phoneDto);
        var newEmployeeDtoRequest = EmployeeDtoRequest.builder()
                .firstName("Sasha")
                .lastName("Borta")
                .jobTittle("sales")
                .departmentId(1L)
                .dateOfBirth(LocalDate.of(1997, 06, 25))
                .gender(Gender.MALE)
                .phones(phones)
                .build();
        var newEmployee = employeeMapper.mapToEmployee(newEmployeeDtoRequest);
        var expected = employeeMapper.mapToEmployeeDtoResponse(newEmployee, departmentDtoResponse);
        expected.getPhones().get(0).setPhoneId(2L);
        expected.setEmployeeId(2L);

        when(departmentApiClient.getDepartmentDtoById(1L)).thenReturn(departmentDtoResponse);

        var actualEmployee = employeeService.save(newEmployeeDtoRequest);

        assertEquals(expected, actualEmployee);

        verify(departmentApiClient, times(2)).getDepartmentDtoById(1L);
        verify(kafkaMessageSender).send(expected);
    }
}