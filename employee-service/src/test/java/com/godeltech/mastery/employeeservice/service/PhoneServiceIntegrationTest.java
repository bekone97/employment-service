package com.godeltech.mastery.employeeservice.service;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.godeltech.mastery.employeeservice.dao.entity.Employee;
import com.godeltech.mastery.employeeservice.dao.entity.Phone;
import com.godeltech.mastery.employeeservice.dataTypeFactory.AdditionalPostgresDataTypeFactory;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoResponse;
import com.godeltech.mastery.employeeservice.dto.PhoneDto;
import com.godeltech.mastery.employeeservice.exception.NotUniqueResourceException;
import com.godeltech.mastery.employeeservice.exception.ResourceNotFoundException;
import com.godeltech.mastery.employeeservice.initializer.DatabaseContainerInitializer;
import com.godeltech.mastery.employeeservice.mapping.PhoneMapper;
import lombok.var;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE,
        dataTypeFactoryClass = AdditionalPostgresDataTypeFactory.class)
@ActiveProfiles("test")
class PhoneServiceIntegrationTest extends DatabaseContainerInitializer {

    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private PhoneService phoneService;

    @Autowired
    private PhoneMapper phoneMapper;

    private EmployeeDtoResponse employee;
    private Phone phone;
    private PhoneDto expected;
    @BeforeEach
    public void setUp() {
    phone=Phone.builder()
            .phoneId(1L)
            .number(297342979)
            .employee(Employee.builder()
                    .employeeId(1L)
                    .build())
            .build();
    employee=EmployeeDtoResponse.builder()
            .employeeId(2L)
            .build();
    expected=phoneMapper.mapToPhoneDto(phone);
    }

    @AfterEach
    public void tearDown(){
        phoneService=null;
        phone=null;
        employee=null;
        expected=null;
    }
    @Test
    void getPhonesByEmployee() {
        List<PhoneDto> phones = new ArrayList<>();
        phones.add(expected);
        employee.setPhones(phones);
        when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        var actual = phoneService.getPhonesByEmployee(1L).stream()
                .findFirst()
                .get();

        assertEquals(expected,actual);
        verify(employeeService).getEmployeeById(1L);
    }
    @Test
    @DataSet(value = {"dataset/init/employee/init.yml", "dataset/init/phone/init.yml"},
            useSequenceFiltering = false,cleanBefore = true,
    executeScriptsBefore = {"scripts/phoneSeq.sql"})
    @ExpectedDataSet(value = "dataset/expected/phone/savePhone.yml")
    void save() {
        expected= PhoneDto.builder()
                .number(289372678)
                .build();
        when(employeeService.getEmployeeById(1L)).thenReturn(employee);


        var actual = phoneService.save(expected,1L);
        expected.setPhoneId(2L);

        assertEquals(expected,actual);


        verify(employeeService).getEmployeeById(1L);
    }
    @Test
    @DataSet(value = {"dataset/init/employee/init.yml", "dataset/init/phone/init.yml"},useSequenceFiltering = false,cleanBefore = true)
    void saveFail() {
        expected.setPhoneId(1L);
        when(employeeService.getEmployeeById(2L)).thenReturn(employee);


        var actual = assertThrows(NotUniqueResourceException.class,
                ()->phoneService.save(expected,2L));

        assertTrue(actual.getMessage().contains("Phone is already exists by number="+expected.getNumber()));


        verify(employeeService,never()).getEmployeeById(2L);
    }

    @Test
    @DataSet(value = {"dataset/init/employee/init.yml", "dataset/init/phone/init.yml"},useSequenceFiltering = false,cleanBefore = true)
    @ExpectedDataSet(value = "dataset/expected/phone/updatePhone.yml")
    void update() {
        int changingNumber = 792526677;
        expected.setNumber(changingNumber);

        var actual= phoneService.update(1L,expected,1L);

        assertEquals(expected,actual);
    }
    @Test
    @DataSet(value = {"dataset/init/employee/init.yml", "dataset/init/phone/init.yml"},useSequenceFiltering = false)
    void updateFail() {

        var actual=assertThrows(NotUniqueResourceException.class,
                ()->phoneService.update(1L,expected,1L));

        assertTrue(actual.getMessage().contains("Phone is already exists by number="+expected.getNumber()));
    }
    @Test
    @DataSet(value = {"dataset/init/employee/init.yml", "dataset/init/phone/init.yml"},useSequenceFiltering = false,cleanBefore = true)
    void updateFailSecond() {
        expected.setNumber(283783273);
        var actual=assertThrows(ResourceNotFoundException.class,
                ()->phoneService.update(5L,expected,2L));

        assertTrue(actual.getMessage().contains("Phone wasn't found by phoneId=5 from Employee with employeeId=2"));
    }


    @Test
    @DataSet(value = {"dataset/init/employee/init.yml", "dataset/init/phone/init.yml"},useSequenceFiltering = false,
            cleanBefore = true)
    @ExpectedDataSet(value = {"dataset/expected/phone/delete.yml"})
    void deleteById() {

        phoneService.deleteById(1L,1L);

    }
    @Test
    @DataSet(value = {"dataset/init/employee/init.yml", "dataset/init/phone/init.yml"},useSequenceFiltering = false,cleanBefore = true)
    void deleteByIdFail() {


        var actual=assertThrows(ResourceNotFoundException.class,
                ()->phoneService.deleteById(5L,2L));

        assertTrue(actual.getMessage().contains("Phone wasn't found by phoneId=5 from Employee with employeeId=2"));

    }
    @Test
    @DataSet(value = {"dataset/init/employee/init.yml", "dataset/init/phone/init.yml"},useSequenceFiltering = false,cleanBefore = true)
    void deleteByIdFailSecond() {


        var actual=assertThrows(ResourceNotFoundException.class,
                ()->phoneService.deleteById(2L,5L));

        assertTrue(actual.getMessage().contains("Phone wasn't found by phoneId=2 from Employee with employeeId=5"));

    }


}