package com.mruruc.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_pk_generator")
    @SequenceGenerator(name = "todo_pk_generator", allocationSize = 1)
    private Long id;
    private String title;
    private String description;
    private LocalDateTime endDate;
    private boolean isCompleted;
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false)
    User user;
}
