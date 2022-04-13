package com.godeltech.mastery.employeeservice.clients.rest;

import com.godeltech.mastery.employeeservice.clients.DepartmentApiClient;
import com.godeltech.mastery.employeeservice.dto.DepartmentDtoResponse;
import com.godeltech.mastery.employeeservice.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
public class RestTemplateDepartmentClientImpl implements DepartmentApiClient {

    private final RestTemplate departmentRestTemplate;

    @Override

    public DepartmentDtoResponse getDepartmentDtoById(Long departmentId) {
        log.info("Get department by id:{}",departmentId);
        return departmentRestTemplate.getForObject("departments/" + departmentId, DepartmentDtoResponse.class);
    }
}
