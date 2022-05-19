package com.godeltech.mastery.employeeservice.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.var;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class TestUtil {

    public static final String CONTENT_TYPE = "application/json";
    public static final String EMPLOYEES_URL = "/employees";
    public static final String EMPLOYEES_WITH_ID_URL = "/employees/{employeeId}";


    public static ResultActions getEmployeesByFirstName(MockMvc mockMvc, String expectedParam) throws Exception {
        return mockMvc.perform(get(EMPLOYEES_URL)
                .header(AUTHORIZATION,getJwtToken())
                .contentType(CONTENT_TYPE)
                .param("firstName", expectedParam));
    }
    public static ResultActions getEmployees(MockMvc mockMvc) throws Exception {
        return mockMvc.perform(get(EMPLOYEES_URL)
                .header(AUTHORIZATION,getJwtToken())
                .contentType(CONTENT_TYPE));
    }

    public static ResultActions getEmployeeById(MockMvc mockMvc, Long employeeId) throws Exception {
        return mockMvc.perform(get(EMPLOYEES_WITH_ID_URL, employeeId)
                .header(AUTHORIZATION,getJwtToken())
                .contentType(CONTENT_TYPE));
    }
    public static ResultActions saveEmployee(MockMvc mockMvc, String employeeDtoRequest) throws Exception {
        return mockMvc.perform(post(EMPLOYEES_URL)
                .header(AUTHORIZATION,getJwtToken())
                .contentType(CONTENT_TYPE)
                .content(employeeDtoRequest));
    }
    public static ResultActions updateEmployee(MockMvc mockMvc, String employeeDtoRequest,Long employeeId) throws Exception {
        return mockMvc.perform(put(EMPLOYEES_WITH_ID_URL, employeeId)
                .header(AUTHORIZATION,getJwtToken())
                .contentType(CONTENT_TYPE)
                .content(employeeDtoRequest));
    }
    public static ResultActions deleteEmployee(MockMvc mockMvc,Long employeeId) throws Exception {
        return mockMvc.perform(delete(EMPLOYEES_WITH_ID_URL, employeeId)
                .header(AUTHORIZATION,getJwtToken())
                .contentType(CONTENT_TYPE));
    }

    private static String getJwtToken() {
        return "Bearer " + JWT.create()
                .withSubject("Artem")
                .withExpiresAt(new Date(currentTimeMillis() + 10 * 10 * 60 * 1000))
                .withIssuer("com.godeltech.mastery")
                .withClaim("userId", 1L)
                .withClaim("roles", "ROLE_ADMIN")
                .sign(Algorithm.HMAC256("secret"));

    }
    public static ResultActions getPhonesByEmployee(MockMvc mockMvc,Long employeeId) throws Exception {
        return mockMvc.perform(get("/employees/{employeeId}/phones",employeeId)
                .header(AUTHORIZATION,getJwtToken())
                .contentType(CONTENT_TYPE));
    }

    public static ResultActions savePhone(MockMvc mockMvc, String phoneDto,Long employeeId) throws Exception {
        return mockMvc.perform(post("/employees/{employeeId}/phones",employeeId)
                .header(AUTHORIZATION,getJwtToken())
                .contentType(CONTENT_TYPE)
                .content(phoneDto));
    }
    public static ResultActions updatePhone(MockMvc mockMvc, String phoneDto,Long employeeId,Long phoneId) throws Exception {
        return mockMvc.perform(put("/employees/{employeeId}/phones/{phoneId}", employeeId,phoneId)
                .header(AUTHORIZATION,getJwtToken())
                .contentType(CONTENT_TYPE)
                .content(phoneDto));
    }
    public static ResultActions deletePhone(MockMvc mockMvc,Long employeeId,Long phoneId) throws Exception {
        return mockMvc.perform(delete("/employees/{employeeId}/phones/{phoneId}", employeeId, phoneId)
                .header(AUTHORIZATION,getJwtToken())
                .contentType(CONTENT_TYPE));
    }
    }
