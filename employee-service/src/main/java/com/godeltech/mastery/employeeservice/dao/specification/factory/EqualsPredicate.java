package com.godeltech.mastery.employeeservice.dao.specification.factory;

import com.godeltech.mastery.employeeservice.dao.specification.Operation;
import com.godeltech.mastery.employeeservice.dao.specification.SearchCriteria;
import liquibase.pro.packaged.T;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Component
public class EqualsPredicate implements PredicateType<T> {

    private static final Operation OPERATION=Operation.EQUALS;

    @Override
    public Operation getOperation() {
        return OPERATION;
    }

    @Override
    public Predicate getPredicate(SearchCriteria searchCriteria, Root<T> root, CriteriaBuilder criteriaBuilder) {
        if (root.get(searchCriteria.getFieldName()).getJavaType() == String.class) {
            return criteriaBuilder.like(root.get(searchCriteria.getFieldName()), "%" + searchCriteria.getFieldValue() + "%");
        } else {
            return criteriaBuilder.equal(root.get(searchCriteria.getFieldName()), searchCriteria.getFieldValue());
        }
    }
}
