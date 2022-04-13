package com.godeltech.mastery.employeeservice.service.impl;

import com.godeltech.mastery.employeeservice.clients.DepartmentApiClient;
import com.godeltech.mastery.employeeservice.dao.EmployeeRepository;
import com.godeltech.mastery.employeeservice.dao.entity.Employee;
import com.godeltech.mastery.employeeservice.dao.specification.EmployeeSpecification;
import com.godeltech.mastery.employeeservice.dao.specification.Operation;
import com.godeltech.mastery.employeeservice.dao.specification.SearchCriteria;
import com.godeltech.mastery.employeeservice.dao.specification.factory.PredicateFactory;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoRequest;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoResponse;
import com.godeltech.mastery.employeeservice.exception.ResourceNotFoundException;
import com.godeltech.mastery.employeeservice.mapping.Mapper;
import com.godeltech.mastery.employeeservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.godeltech.mastery.employeeservice.utils.ConstantUtil.Exception.EMPLOYEE_ID_FOR_EXCEPTION;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;
    private final Mapper mapper;
    private final PredicateFactory<Employee> predicateFactory;
    private final DepartmentApiClient departmentApiClient;


    public List<EmployeeDtoResponse> getEmployees() {
        log.debug("Get all employees");

        return employeeRepository.findAll().stream()
                .map(employee -> mapper.mapToEmployeeDtoResponse(employee, departmentApiClient.getDepartmentDtoById(employee.getDepartmentId())))
                .collect(Collectors.toList());
    }


    public EmployeeDtoResponse getEmployeeById(Long employeeId) {
        log.debug("Get employee  by employeeId :{}",
                employeeId);

        return employeeRepository.findById(employeeId)
                .map(employee -> mapper.mapToEmployeeDtoResponse(employee, departmentApiClient.getDepartmentDtoById(employee.getDepartmentId())))
                .orElseThrow(() -> {
                    log.error("Employee with id={} was not found",
                            employeeId);
                    return new ResourceNotFoundException(Employee.class,
                            EMPLOYEE_ID_FOR_EXCEPTION, employeeId);
                });
    }

    public EmployeeDtoResponse save(EmployeeDtoRequest employeeDtoRequest) {
        log.debug("Save a new employee :{}", employeeDtoRequest);
        checkExistingDepartment(employeeDtoRequest);

        var employee = employeeRepository.save(mapper.mapToEmployee(employeeDtoRequest));

        return mapper.mapToEmployeeDtoResponse(employee, departmentApiClient.getDepartmentDtoById(employee.getDepartmentId()));
    }


    public EmployeeDtoResponse update(Long employeeId, EmployeeDtoRequest employeeDtoRequest) {
        log.debug("Check existing employee by employeeId :{} and update it by :{}",
                employeeId, employeeDtoRequest);

        return employeeRepository.findById(employeeId)
                .map(emp -> {
                    var employee = mapper.mapToEmployee(employeeDtoRequest);
                    employee.setEmployeeId(employeeId);
                    return employee;
                })
                .map(employeeRepository::save)
                .map(emp->mapper.mapToEmployeeDtoResponse(emp, departmentApiClient.getDepartmentDtoById(emp.getDepartmentId())))
                .orElseThrow(() -> {
                    log.error("Employee with id={} was not found",
                            employeeId);
                    return new ResourceNotFoundException(Employee.class,
                            EMPLOYEE_ID_FOR_EXCEPTION, employeeId);
                });
    }

    public void deleteById(Long employeeId) {
        log.debug("Check existing employee by employeeId :{} and remove it", employeeId);

        var employee = getEmployeeById(employeeId);

        employeeRepository.deleteById(employee.getEmployeeId());
    }

    public List<EmployeeDtoResponse> findByFirstNameLike(String firstName) {
        log.debug("Search employees by a part of firstName:{}", firstName);

        var specification = new EmployeeSpecification(
                new SearchCriteria("firstName", Operation.EQUALS, firstName), predicateFactory);
        return employeeRepository.findAll(specification).stream()
                .map(emp->mapper.mapToEmployeeDtoResponse(emp, departmentApiClient.getDepartmentDtoById(emp.getDepartmentId())))
                .collect(Collectors.toList());
    }

    private void checkExistingDepartment(EmployeeDtoRequest employeeDtoRequest) {
       departmentApiClient.getDepartmentDtoById(employeeDtoRequest.getDepartmentId());
    }
}
