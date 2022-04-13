package com.godeltech.mastery.employeeservice.dao.specification;

import com.godeltech.mastery.employeeservice.dao.entity.Employee;
import com.godeltech.mastery.employeeservice.dao.specification.factory.PredicateFactory;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Data
@RequiredArgsConstructor
public class EmployeeSpecification implements Specification<Employee> {

    private final SearchCriteria searchCriteria;
    private final PredicateFactory<Employee> predicateFactory;



    @Override
    public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return predicateFactory.getPredicateType(searchCriteria,root,criteriaBuilder);
    }
}
