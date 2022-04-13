package com.godeltech.mastery.departmentservice.service;

import com.godeltech.mastery.departmentservice.dto.DepartmentDtoResponse;

public interface DepartmentService {

    DepartmentDtoResponse findDepartmentById(Long departmentId);
}
