package com.godeltech.mastery.securityservice.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refresh_token",schema = "jwt")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;

    @Column(length = 528)
    private String token;

    @Column
    private Timestamp expires;

    @Column
    private Timestamp created;

    @Column
    private Timestamp revoked;

    @Column(name = "replaced_by_token")
    private String replacedByToken;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "user_id")
    private Long userId;
}
