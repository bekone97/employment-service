package com.godeltech.mastery.employeeservice.mapping;

import com.godeltech.mastery.employeeservice.dao.entity.Employee;
import com.godeltech.mastery.employeeservice.dto.DepartmentDtoResponse;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoRequest;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoResponse;

public interface EmployeeMapper {
    Employee mapToEmployee(EmployeeDtoRequest employeeDtoRequest);

    Employee mapToEmployee(EmployeeDtoRequest employeeDtoRequest, Long employeeId);

    EmployeeDtoResponse mapToEmployeeDtoResponse(Employee employee, DepartmentDtoResponse departmentDtoResponse);

    Employee initEmployeeWithId(Long employeeId);


}

