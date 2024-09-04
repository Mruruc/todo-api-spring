package com.mruruc.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users",uniqueConstraints = {
        @UniqueConstraint(name = "unique_email_constraint",columnNames = {"email"})
})
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_pk_generator")
    @SequenceGenerator(name = "todo_pk_generator" ,allocationSize = 1)
    @Column(name = "user_id")
    private Long userId;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    private boolean isAccountLocked;
    private boolean isAccountEnabled;
    @CreatedDate
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",joinColumns ={
            @JoinColumn(name = "user_id",nullable = false)
    },inverseJoinColumns ={
            @JoinColumn(name = "role_id")
    } )
    private List<Role> roles;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    List<Todo> todos;
}
