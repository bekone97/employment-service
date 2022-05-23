package com.godeltech.mastery.employeeservice.dao.specification.factory;

import com.godeltech.mastery.employeeservice.dao.specification.Operation;
import com.godeltech.mastery.employeeservice.dao.specification.SearchCriteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface PredicateType<T> {

    Operation getOperation();

    Predicate getPredicate(SearchCriteria searchCriteria, Root<T> root, CriteriaBuilder criteriaBuilder);

}
