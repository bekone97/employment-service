package com.godeltech.mastery.employeeservice.utils;


import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class TestUtil {

    public static final String CONTENT_TYPE = "application/json";
    public static final String EMPLOYEES_URL = "/employees";
    public static final String EMPLOYEES_WITH_ID_URL = "/employees/{employeeId}";

    public static ResultActions getEmployeesByFirstName(MockMvc mockMvc, String expectedParam) throws Exception {
        return mockMvc.perform(get(EMPLOYEES_URL)
                .contentType(CONTENT_TYPE)
                .param("firstName", expectedParam));
    }
    public static ResultActions getEmployees(MockMvc mockMvc) throws Exception {
        return mockMvc.perform(get(EMPLOYEES_URL)
                .contentType(CONTENT_TYPE));
    }

    public static ResultActions getEmployeeById(MockMvc mockMvc, Long employeeId) throws Exception {
        return mockMvc.perform(get(EMPLOYEES_WITH_ID_URL, employeeId)
                .contentType(CONTENT_TYPE));
    }
    public static ResultActions saveEmployee(MockMvc mockMvc, String employeeDtoRequest) throws Exception {
        return mockMvc.perform(post(EMPLOYEES_URL)
                .contentType(CONTENT_TYPE)
                .content(employeeDtoRequest));
    }
    public static ResultActions updateEmployee(MockMvc mockMvc, String employeeDtoRequest,Long employeeId) throws Exception {
        return mockMvc.perform(put(EMPLOYEES_WITH_ID_URL, employeeId)
                .contentType(CONTENT_TYPE)
                .content(employeeDtoRequest));
    }
    public static ResultActions deleteEmployee(MockMvc mockMvc,Long employeeId) throws Exception {
        return mockMvc.perform(delete(EMPLOYEES_WITH_ID_URL, employeeId)
                .contentType(CONTENT_TYPE));
    }
    }
