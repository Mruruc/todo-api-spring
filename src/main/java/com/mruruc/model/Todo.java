package com.mruruc.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "todos")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "todo_pk_generator")
    @SequenceGenerator(name = "todo_pk_generator" ,allocationSize = 1)
    private Long toDoId;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @ManyToOne(fetch = FetchType.LAZY,targetEntity = User.class)
    @JoinColumn(name = "user_id",nullable = false)
    User user;
}
