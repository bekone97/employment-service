package com.godeltech.mastery.employeeservice.service.impl;

import com.godeltech.mastery.employeeservice.clients.DepartmentApiClient;
import com.godeltech.mastery.employeeservice.dao.EmployeeRepository;
import com.godeltech.mastery.employeeservice.dao.entity.Employee;
import com.godeltech.mastery.employeeservice.dao.entity.Phone;
import com.godeltech.mastery.employeeservice.dao.specification.EmployeeSpecification;
import com.godeltech.mastery.employeeservice.dao.specification.Operation;
import com.godeltech.mastery.employeeservice.dao.specification.SearchCriteria;
import com.godeltech.mastery.employeeservice.dao.specification.factory.PredicateFactory;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoRequest;
import com.godeltech.mastery.employeeservice.dto.EmployeeDtoResponse;
import com.godeltech.mastery.employeeservice.exception.ResourceNotFoundException;
import com.godeltech.mastery.employeeservice.mapping.EmployeeMapper;
import com.godeltech.mastery.employeeservice.service.EmployeeService;
import com.godeltech.mastery.employeeservice.service.KafkaMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static com.godeltech.mastery.employeeservice.utils.ConstantUtil.Exception.EMPLOYEE_ID_FOR_EXCEPTION;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final PredicateFactory<Employee> predicateFactory;
    private final DepartmentApiClient departmentApiClient;
    private final KafkaMessageSender kafkaMessageSender;


    public List<EmployeeDtoResponse> getEmployees() {
        log.debug("Get all employees");
        var employees =employeeRepository.findAll();
        return employees.stream()
                .map(employee -> employeeMapper.mapToEmployeeDtoResponse(employee, departmentApiClient.getDepartmentDtoById(employee.getDepartmentId())))
                .collect(Collectors.toList());

    }

    public EmployeeDtoResponse getEmployeeById(Long employeeId) {
        log.debug("Get employee  by employeeId :{}",
                employeeId);

        return employeeRepository.findById(employeeId)
                .map(employee -> employeeMapper.mapToEmployeeDtoResponse(employee, departmentApiClient.getDepartmentDtoById(employee.getDepartmentId())))
                .orElseThrow(() -> {
                    log.error("Employee with id={} was not found",
                            employeeId);
                    return new ResourceNotFoundException(Employee.class,
                            EMPLOYEE_ID_FOR_EXCEPTION, employeeId);
                });
    }

    @Transactional
    public EmployeeDtoResponse save(EmployeeDtoRequest employeeDtoRequest) throws ExecutionException, InterruptedException, TimeoutException {
        log.debug("Save a new employee :{}", employeeDtoRequest);
        checkExistingDepartment(employeeDtoRequest);
        var emp= employeeMapper.mapToEmployee(employeeDtoRequest);
        var employee = employeeRepository.save(emp);

        var employeeDtoResponse = employeeMapper.mapToEmployeeDtoResponse(employee, departmentApiClient.getDepartmentDtoById(employee.getDepartmentId()));
        kafkaMessageSender.send(employeeDtoResponse);
        return employeeDtoResponse;
    }

    @Transactional
    public EmployeeDtoResponse update(Long employeeId, EmployeeDtoRequest employeeDtoRequest) {
        log.debug("Check existing employee by employeeId :{} and update it by :{}",
                employeeId, employeeDtoRequest);

        return employeeRepository.findById(employeeId)
                .map(emp -> employeeMapper.mapToEmployee(employeeDtoRequest, employeeId))
                .map(employeeRepository::save)
                .map(emp -> employeeMapper.mapToEmployeeDtoResponse(emp, departmentApiClient.getDepartmentDtoById(emp.getDepartmentId())))
                .orElseThrow(() -> {
                    log.error("Employee with id={} was not found",
                            employeeId);
                    return new ResourceNotFoundException(Employee.class,
                            EMPLOYEE_ID_FOR_EXCEPTION, employeeId);
                });
    }
    @Transactional
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
                .map(emp -> employeeMapper.mapToEmployeeDtoResponse(emp, departmentApiClient.getDepartmentDtoById(emp.getDepartmentId())))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmployeeId(Long employeeId) {
        return employeeRepository.existsById(employeeId);
    }

    private void checkExistingDepartment(EmployeeDtoRequest employeeDtoRequest) {
        departmentApiClient.getDepartmentDtoById(employeeDtoRequest.getDepartmentId());
    }
}
