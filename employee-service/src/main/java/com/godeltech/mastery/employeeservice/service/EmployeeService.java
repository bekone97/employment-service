package com.godeltech.mastery.employeeservice.service;

import com.godeltech.mastery.employeeservice.dto.EmployeeDtoRequest;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoResponse;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDtoResponse> getEmployees();

    EmployeeDtoResponse save(EmployeeDtoRequest employeeDtoRequest);

    EmployeeDtoResponse update(Long employeeId, EmployeeDtoRequest employeeDtoRequest);

    void deleteById(Long employeeId);

    EmployeeDtoResponse getEmployeeById(Long employeeId);

    List<EmployeeDtoResponse> findByFirstNameLike(String firstName);
}
