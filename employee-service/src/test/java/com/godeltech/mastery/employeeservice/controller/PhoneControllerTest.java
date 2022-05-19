package com.godeltech.mastery.employeeservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godeltech.mastery.employeeservice.dao.entity.Employee;
import com.godeltech.mastery.employeeservice.dao.entity.Phone;
import com.godeltech.mastery.employeeservice.dto.PhoneDto;
import com.godeltech.mastery.employeeservice.exception.NotUniqueResourceException;
import com.godeltech.mastery.employeeservice.exception.ResourceNotFoundException;
import com.godeltech.mastery.employeeservice.service.PhoneService;
import com.godeltech.mastery.employeeservice.utils.TestUtil;
import lombok.var;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PhoneController.class)
class PhoneControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PhoneService phoneService;

    private PhoneDto phoneDto;

    @BeforeEach
    public void beforeEach(){
        phoneDto= PhoneDto.builder()
                .phoneId(1L)
                .number(297342979)
                .build();
    }
    @AfterEach
    public void tearDown(){
        phoneDto=null;
    }
    @Test
    void getPhones() throws Exception {
        var expected = Collections.singletonList(phoneDto);
        when(phoneService.getPhonesByEmployee(1L)).thenReturn(expected);

        var actual = TestUtil.getPhonesByEmployee(mockMvc,1L)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected),actual);
        verify(phoneService).getPhonesByEmployee(1L);
    }

    @Test
    void savePhone() throws Exception {
        var expected = phoneDto;
        when(phoneService.save(phoneDto,1L)).thenReturn(expected);

        var actual = TestUtil.savePhone(mockMvc,objectMapper.writeValueAsString(phoneDto),1L)
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected),actual);
        verify(phoneService).save(phoneDto,1L);
    }
    @Test
    void savePhoneFailNumber() throws Exception {
        var expectedMessage ="Phone is already exists by number="+phoneDto.getNumber();
        when(phoneService.save(phoneDto,1L)).thenThrow(new NotUniqueResourceException(Phone.class,"number",phoneDto.getNumber()));

        TestUtil.savePhone(mockMvc,objectMapper.writeValueAsString(phoneDto),1L)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotUniqueResourceException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains(expectedMessage)));

        verify(phoneService).save(phoneDto,1L);
    }
    @Test
    void savePhoneFailEmployeeId() throws Exception {
        var expectedMessage ="Employee wasn't found by employeeId";
        when(phoneService.save(phoneDto,1L)).thenThrow(new ResourceNotFoundException(Employee.class,"employeeId",1L));

        TestUtil.savePhone(mockMvc,objectMapper.writeValueAsString(phoneDto),1L)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains(expectedMessage)));

        verify(phoneService).save(phoneDto,1L);
    }

    @Test
    void updatePhone() throws Exception {
        var expected= phoneDto;
        when(phoneService.update(phoneDto.getPhoneId(),phoneDto,1L)).thenReturn(phoneDto);

        var actual =TestUtil.updatePhone(mockMvc,objectMapper.writeValueAsString(phoneDto),1L,phoneDto.getPhoneId())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expected),actual);
        verify(phoneService).update(phoneDto.getPhoneId(),phoneDto,1L);
    }

    @Test
    void updatePhoneFailId() throws Exception {
        var expectedMessage = "Phone wasn't found by phoneId=1 from Employee with employeeId=1";
        when(phoneService.update(phoneDto.getPhoneId(), phoneDto, 1L))
                .thenThrow(new ResourceNotFoundException(Phone.class, "phoneId", phoneDto.getPhoneId(), Employee.class, "employeeId", 1L));

        TestUtil.updatePhone(mockMvc, objectMapper.writeValueAsString(phoneDto), 1L, phoneDto.getPhoneId())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains(expectedMessage)));

        verify(phoneService).update(phoneDto.getPhoneId(), phoneDto, 1L);
    }

    @Test
    void deletePhone() throws Exception {
        TestUtil.deletePhone(mockMvc,1L,phoneDto.getPhoneId())
                .andExpect(status().isOk());

        verify(phoneService).deleteById(1L,phoneDto.getPhoneId());
    }
}