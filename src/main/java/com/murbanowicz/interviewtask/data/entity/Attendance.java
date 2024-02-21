package com.murbanowicz.interviewtask.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime entryDate;
    private LocalDateTime exitDate;
    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;
}