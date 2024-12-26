package com.mruruc.security.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refresh_tokens",
        uniqueConstraints = {
                @UniqueConstraint(name = "uniq_token_constraint", columnNames = {"token"}),
                @UniqueConstraint(name = "uniq_username_constraint", columnNames = {"username"})
        })
@EntityListeners(AuditingEntityListener.class)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_pk")
    @SequenceGenerator(name = "refresh_token_pk", allocationSize = 1)
    private Long tokenId;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @Column(nullable = false)
    private boolean isRevoked;

    private String replacedBy;
    private String ipAddress;
    private String userAgent;

    @Column(nullable = false,unique = true)
    private String username;


}
