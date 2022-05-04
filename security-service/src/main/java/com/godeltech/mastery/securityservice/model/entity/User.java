package com.godeltech.mastery.securityservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Table(name = "user",schema = "jwt")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TypeDef(name = "postgreSqlEnumType", typeClass = PostgreSqlEnumType.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "role_type")
    @Type(type = "postgreSqlEnumType")
    private Role role;
}
