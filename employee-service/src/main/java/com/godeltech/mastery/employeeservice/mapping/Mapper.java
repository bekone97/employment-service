package com.godeltech.mastery.employeeservice.mapping;

import com.godeltech.mastery.employeeservice.dao.entity.Employee;
import com.godeltech.mastery.employeeservice.dto.DepartmentDtoResponse;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoRequest;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoResponse;

public interface Mapper {
    Employee mapToEmployee(EmployeeDtoRequest employeeDtoRequest);
    EmployeeDtoResponse mapToEmployeeDtoResponse(Employee employee);
    EmployeeDtoResponse mapToEmployeeDtoResponse(Employee employee, DepartmentDtoResponse departmentDtoResponse);
}
