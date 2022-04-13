package com.godeltech.mastery.employeeservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godeltech.mastery.employeeservice.controller.EmployeeController;
import com.godeltech.mastery.employeeservice.dao.entity.Employee;
import com.godeltech.mastery.employeeservice.dao.entity.Gender;
import com.godeltech.mastery.employeeservice.dto.DepartmentDtoResponse;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoRequest;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoResponse;
import com.godeltech.mastery.employeeservice.exception.ResourceNotFoundException;
import com.godeltech.mastery.employeeservice.service.EmployeeService;
import com.godeltech.mastery.employeeservice.utils.TestUtil;
import lombok.var;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    private EmployeeDtoResponse employeeDtoResponse;
    private EmployeeDtoRequest employeeDtoRequest;
    Long employeeId;

    @BeforeEach
    public void setUp() {
        employeeDtoResponse = EmployeeDtoResponse.builder()
                .employeeId(1L)
                .firstName("Artem")
                .lastName("Myachin")
                .department(DepartmentDtoResponse.builder()
                        .departmentId(1L)
                        .departmentName("Security")
                        .build())
                .jobTittle("IT")
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.parse("1997-06-25"))
                .build();
        employeeDtoRequest = EmployeeDtoRequest.builder()
                .firstName(employeeDtoResponse.getFirstName())
                .lastName(employeeDtoResponse.getLastName())
                .departmentId(employeeDtoResponse.getDepartment().getDepartmentId())
                .jobTittle(employeeDtoResponse.getJobTittle())
                .gender(employeeDtoResponse.getGender())
                .dateOfBirth(employeeDtoResponse.getDateOfBirth())
                .build();
        employeeId = 1L;
    }

    @AfterEach
    public void tearDown() {
        employeeDtoResponse = null;
        employeeDtoRequest = null;
        employeeId = null;
    }

    @Test
    void getEmployeesWithoutParam() throws Exception {
        var expected = Collections.singletonList(employeeDtoResponse);
        when(employeeService.getEmployees()).thenReturn(expected);

        var actual = TestUtil.getEmployees(mockMvc)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), actual);
        verify(employeeService).getEmployees();
        verify(employeeService, never()).findByFirstNameLike("");
    }

    @Test
    void getEmployeesWithFirstNameParam() throws Exception {
        String expectedParam = "Artem";
        var expected = Collections.singletonList(employeeDtoResponse);
        when(employeeService.findByFirstNameLike(expectedParam)).thenReturn(expected);

        var actual = TestUtil.getEmployeesByFirstName(mockMvc, expectedParam)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), actual);
        verify(employeeService, never()).getEmployees();
        verify(employeeService).findByFirstNameLike(expectedParam);
    }

    @Test
    void getEmployeeById() throws Exception {
        var expected = employeeDtoResponse;
        when(employeeService.getEmployeeById(employeeId)).thenReturn(employeeDtoResponse);

        var actual = TestUtil.getEmployeeById(mockMvc, employeeId)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), actual);
        verify(employeeService).getEmployeeById(employeeId);
    }

    @Test
    void getEmployeeByIdNoEmployee() throws Exception {
        var expectedMessage = "Employee wasn't found by employeeId";
        when(employeeService.getEmployeeById(employeeId)).thenThrow(new ResourceNotFoundException(Employee.class,"employeeId",employeeId));

        TestUtil.getEmployeeById(mockMvc, employeeId)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains(expectedMessage)));

        verify(employeeService).getEmployeeById(employeeId);
    }

    @Test
    void getEmployeeByIdFailWrongEmployeeId() throws Exception {
        String expectedMessage = "{general.validation.validId.positive}";
        employeeId = -2L;

        TestUtil.getEmployeeById(mockMvc, employeeId)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains(expectedMessage)));

        verify(employeeService, never()).getEmployeeById(employeeId);
    }


    @Test
    void saveEmployee() throws Exception {
        var expected = employeeDtoResponse;
        when(employeeService.save(employeeDtoRequest)).thenReturn(expected);

        var actual = TestUtil.saveEmployee(mockMvc, objectMapper.writeValueAsString(employeeDtoRequest))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), actual);
        verify(employeeService).save(employeeDtoRequest);
    }

    @Test
    void saveEmployeeWrongValidation() throws Exception {
        var expectedMessage = "{employee.validation.gender.notNull}";
        employeeDtoRequest.setGender(null);

       TestUtil.saveEmployee(mockMvc, objectMapper.writeValueAsString(employeeDtoRequest))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains(expectedMessage)));


        verify(employeeService, never()).save(employeeDtoRequest);

    }

    @Test
    void updateEmployee() throws Exception {
        var expected = employeeDtoResponse;
        when(employeeService.update(employeeId, employeeDtoRequest)).thenReturn(expected);

        var actual = TestUtil.updateEmployee(mockMvc,
                        objectMapper.writeValueAsString(employeeDtoRequest),employeeId)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected), actual);
        verify(employeeService).update(employeeId, employeeDtoRequest);
    }

    @Test
    void updateEmployeeNoEmployeeWithId() throws Exception {
        var expectedMessage = "Employee wasn't found by employeeId";
        when(employeeService.update(employeeId, employeeDtoRequest)).thenThrow(new ResourceNotFoundException(Employee.class,"employeeId",employeeId));

            var object =TestUtil.updateEmployee(mockMvc,
                            objectMapper.writeValueAsString(employeeDtoRequest), employeeId)
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                    .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains(expectedMessage)))
                    .andReturn().getResponse().getContentAsString();

            verify(employeeService).update(employeeId, employeeDtoRequest);

    }

    @Test
    void updateEmployeeWrongValid() throws Exception {
        String expectedMessage = "{employee.validation.jobTittle.notBlank}";
        employeeDtoRequest.setJobTittle(null);

        TestUtil.updateEmployee(mockMvc,
                        objectMapper.writeValueAsString(employeeDtoRequest), employeeId)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains(expectedMessage)));

        verify(employeeService, never()).update(employeeId, employeeDtoRequest);

    }

    @Test
    void updateEmployeeWrongId() throws Exception {
        String expectedMessage = "{general.validation.validId.positive}";
        employeeId = -2L;

        TestUtil.updateEmployee(mockMvc,
                        objectMapper.writeValueAsString(employeeDtoRequest), employeeId)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains(expectedMessage)))
                .andReturn().getResponse().getContentAsString();

        verify(employeeService, never()).update(employeeId, employeeDtoRequest);
    }

    @Test
    void deleteEmployee() throws Exception {

        TestUtil.deleteEmployee(mockMvc,employeeId)
                .andExpect(status().isOk());

        verify(employeeService).deleteById(employeeId);
    }

    @Test
    void deleteEmployeeWrongId() throws Exception {
        String expectedMessage = "{general.validation.validId.positive}";
        String timestampFormat = "2000-04-05T11:12:13";
        LocalDateTime timestamp = LocalDateTime.parse(timestampFormat);
        employeeId = -2L;
        try (MockedStatic<LocalDateTime> time = Mockito.mockStatic(LocalDateTime.class)) {
            time.when(LocalDateTime::now).thenReturn(timestamp);

            TestUtil.deleteEmployee(mockMvc, employeeId)
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
                    .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains(expectedMessage)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$['timestamp']", is(timestampFormat)))
                    .andExpect(jsonPath("$['message']", is("Validation error")));

            verify(employeeService, never()).deleteById(employeeId);
        }
    }
}