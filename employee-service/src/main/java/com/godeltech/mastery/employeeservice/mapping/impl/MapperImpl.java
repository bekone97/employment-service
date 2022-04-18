package com.godeltech.mastery.employeeservice.mapping.impl;

import com.godeltech.mastery.employeeservice.dao.entity.Employee;
import com.godeltech.mastery.employeeservice.dto.DepartmentDtoResponse;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoRequest;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoResponse;
import com.godeltech.mastery.employeeservice.mapping.Mapper;
import lombok.var;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperImpl implements Mapper {

    private final ModelMapper modelMapper;

    public MapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        var propertyMapper = this.modelMapper.createTypeMap(EmployeeDtoRequest.class,Employee.class);
        propertyMapper.addMappings(mapping -> mapping.skip(Employee::setEmployeeId));
    }

    @Override
    public Employee mapToEmployee(EmployeeDtoRequest employeeDtoRequest) {
        var employee= modelMapper.map(employeeDtoRequest,Employee.class);
        return employee;
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
