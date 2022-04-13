package com.godeltech.mastery.employeeservice.clients;

import com.godeltech.mastery.employeeservice.dto.DepartmentDtoResponse;
import org.springframework.cache.annotation.Cacheable;

public interface DepartmentApiClient {

    @Cacheable(value = "departments",key = "#departmentId")
    DepartmentDtoResponse getDepartmentDtoById(Long departmentId);

}
