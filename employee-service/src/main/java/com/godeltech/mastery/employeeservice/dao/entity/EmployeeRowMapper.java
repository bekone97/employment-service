package com.godeltech.mastery.employeeservice.dao.entity;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static com.godeltech.mastery.employeeservice.utils.ConstantUtil.Employee.*;


public class EmployeeRowMapper implements RowMapper<Employee> {


    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Employee.builder()
                .employeeId(rs.getLong(EMPLOYEE_ID))
                .firstName(rs.getString(FIRST_NAME))
                .lastName(rs.getString(LAST_NAME))
                .departmentId(rs.getLong(DEPARTMENT_ID))
                .gender(Gender.valueOf(rs.getString(GENDER)))
                .dateOfBirth(LocalDate.parse(rs.getString(DATE_OF_BIRTH)))
                .jobTittle(rs.getString(JOB_TITTLE))
                .build();

    }
}
