package com.godeltech.mastery.employeeservice.dao.specification.factory;

import com.godeltech.mastery.employeeservice.dao.specification.Operation;
import com.godeltech.mastery.employeeservice.dao.specification.SearchCriteria;
import liquibase.pro.packaged.T;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static com.godeltech.mastery.employeeservice.utils.ConstantUtil.Exception.NO_PREDICATE_TYPE;

public class NotExistPredicate implements PredicateType<T> {

    @Override
    public Operation getOperation() {
        throw new RuntimeException(NO_PREDICATE_TYPE);
    }

    @Override
    public Predicate getPredicate(SearchCriteria searchCriteria, Root<T> root, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
