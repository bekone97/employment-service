package com.godeltech.mastery.employeeservice.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TypeDef(name = "postgreSqlEnumType", typeClass = PostgreSqlEnumType.class)
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "job_tittle")
    private String jobTittle;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "gender_type")
    @Type(type = "postgreSqlEnumType")
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

}
