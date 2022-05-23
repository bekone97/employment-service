package com.godeltech.mastery.departmentservice.controller;

import com.godeltech.mastery.departmentservice.dto.DepartmentDtoResponse;
import com.godeltech.mastery.departmentservice.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;


    @GetMapping("/{departmentId}")
    @ResponseStatus(HttpStatus.OK)
    public DepartmentDtoResponse getEmployeeById(@PathVariable Long departmentId) {
        return departmentService.findDepartmentById(departmentId);

    }
}
