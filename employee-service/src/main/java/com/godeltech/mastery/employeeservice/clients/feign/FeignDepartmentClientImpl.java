package com.godeltech.mastery.employeeservice.clients.feign;

import com.godeltech.mastery.employeeservice.clients.DepartmentApiClient;
import com.godeltech.mastery.employeeservice.dto.DepartmentDtoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class FeignDepartmentClientImpl implements DepartmentApiClient {

    private final FeignDepartmentClient feignDepartmentClient;

    @Override
    public DepartmentDtoResponse getDepartmentDtoById(Long departmentId) {
        log.info("Get department by id:{}", departmentId);
        return feignDepartmentClient.getDepartmentById(departmentId);
    }
}
