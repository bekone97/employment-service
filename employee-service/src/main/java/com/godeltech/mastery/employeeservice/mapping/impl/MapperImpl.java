package com.godeltech.mastery.employeeservice.mapping.impl;

import com.godeltech.mastery.employeeservice.dao.entity.Employee;
import com.godeltech.mastery.employeeservice.dto.DepartmentDtoResponse;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoRequest;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoResponse;
import com.godeltech.mastery.employeeservice.mapping.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperImpl implements Mapper {

    private final ModelMapper modelMapper;
    @Override
    public Employee mapToEmployee(EmployeeDtoRequest employeeDtoRequest) {
        return modelMapper.map(employeeDtoRequest,Employee.class);
    }

    @Override
    public EmployeeDtoResponse mapToEmployeeDtoResponse(Employee employee) {
        return modelMapper.map(employee,EmployeeDtoResponse.class);
    }

    @Override
    public EmployeeDtoResponse mapToEmployeeDtoResponse(Employee employee, DepartmentDtoResponse departmentDtoResponse) {
        var employeeDtoResponse = modelMapper.map(employee,EmployeeDtoResponse.class);
        employeeDtoResponse.setDepartment(departmentDtoResponse);
        return employeeDtoResponse;
    }
}
