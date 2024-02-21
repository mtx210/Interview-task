package com.murbanowicz.interviewtask.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;
    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false)
    private School school;
    @OneToMany(mappedBy = "child")
    private Set<Attendance> attendances;
}