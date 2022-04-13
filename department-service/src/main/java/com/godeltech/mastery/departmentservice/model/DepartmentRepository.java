package com.godeltech.mastery.departmentservice.model;

import com.godeltech.mastery.departmentservice.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
}
