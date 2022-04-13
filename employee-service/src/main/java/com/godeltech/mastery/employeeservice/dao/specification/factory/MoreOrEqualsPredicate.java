package com.godeltech.mastery.employeeservice.dao.specification.factory;

import com.godeltech.mastery.employeeservice.dao.specification.Operation;
import com.godeltech.mastery.employeeservice.dao.specification.SearchCriteria;
import liquibase.pro.packaged.T;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Component
public class MoreOrEqualsPredicate implements PredicateType<T> {

    private static final Operation OPERATION = Operation.MORE_OR_EQUALS;

    @Override
    public Operation getOperation() {
        return OPERATION;
    }

    @Override
    public Predicate getPredicate(SearchCriteria searchCriteria, Root<T> root, CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.greaterThanOrEqualTo(
                root.get(searchCriteria.getFieldName()), searchCriteria.getFieldValue().toString());
    }
}
