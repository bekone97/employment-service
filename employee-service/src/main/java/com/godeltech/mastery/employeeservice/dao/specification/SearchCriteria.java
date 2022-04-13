package com.godeltech.mastery.employeeservice.dao.specification;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class SearchCriteria {

    private String fieldName;

    private Operation operation;

    private Object fieldValue;

}
