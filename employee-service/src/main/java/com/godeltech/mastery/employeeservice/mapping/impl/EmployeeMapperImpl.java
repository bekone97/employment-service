package com.godeltech.mastery.employeeservice.mapping.impl;

import com.godeltech.mastery.employeeservice.dao.entity.Employee;
import com.godeltech.mastery.employeeservice.dto.DepartmentDtoResponse;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoRequest;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoResponse;
import com.godeltech.mastery.employeeservice.mapping.EmployeeMapper;
import lombok.var;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    private final ModelMapper modelMapper;

    public EmployeeMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        var propertyMapperEmployee = this.modelMapper.createTypeMap(EmployeeDtoRequest.class,Employee.class);
        propertyMapperEmployee.addMappings(mapping -> mapping.skip(Employee::setEmployeeId));
    }

    @Override
    public Employee mapToEmployee(EmployeeDtoRequest employeeDtoRequest) {
      var employee=modelMapper.map(employeeDtoRequest,Employee.class);
      employee.getPhones().forEach(phone -> phone.setEmployee(employee));
      return employee;
    }

    @Override
    public Employee mapToEmployee(EmployeeDtoRequest employeeDtoRequest, Long employeeId) {
        var employee= modelMapper.map(employeeDtoRequest,Employee.class);
        employee.setEmployeeId(employeeId);
        employee.getPhones().forEach(phone -> phone.setEmployee(employee));
        return employee;
    }


    @Override
    public EmployeeDtoResponse mapToEmployeeDtoResponse(Employee employee, DepartmentDtoResponse departmentDtoResponse) {
        var employeeDtoResponse = modelMapper.map(employee,EmployeeDtoResponse.class);
        employeeDtoResponse.setDepartment(departmentDtoResponse);
        return employeeDtoResponse;
    }

    @Override
    public Employee initEmployeeWithId(Long employeeId) {
        return Employee.builder()
                .employeeId(employeeId)
                .build();
    }


}
