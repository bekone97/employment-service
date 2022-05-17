package com.godeltech.mastery.employeeservice.clients.feign;

import com.godeltech.mastery.employeeservice.dto.DepartmentDtoResponse;
import com.godeltech.mastery.employeeservice.interceptors.FeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(value = "departmentFeignClient",url = "${client.department.base.url}",configuration = FeignInterceptor.class)
public interface FeignDepartmentClient {

    @GetMapping("departments/{departmentId}")
    DepartmentDtoResponse getDepartmentById(@PathVariable Long departmentId);
}
