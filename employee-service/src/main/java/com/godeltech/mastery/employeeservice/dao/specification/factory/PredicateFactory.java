package com.godeltech.mastery.employeeservice.dao.specification.factory;

import com.godeltech.mastery.employeeservice.dao.specification.Operation;
import com.godeltech.mastery.employeeservice.dao.specification.SearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public class PredicateFactory<T> {

    private final Map<Operation, PredicateType<T>> predicateContext;

    @Autowired
    public PredicateFactory(List<PredicateType<T>> predicateTypes) {
        predicateContext= predicateTypes.stream().collect(toMap(PredicateType<T>::getOperation,Function.identity()));
    }

    public Predicate getPredicateType(SearchCriteria searchCriteria, Root<T> root, CriteriaBuilder criteriaBuilder){
       return predicateContext.getOrDefault(searchCriteria.getOperation(), (PredicateType<T>) new NotExistPredicate())
               .getPredicate(searchCriteria,root,criteriaBuilder);
    }
}
