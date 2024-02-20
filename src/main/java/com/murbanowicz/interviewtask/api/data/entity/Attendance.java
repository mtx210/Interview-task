package com.murbanowicz.interviewtask.api.data.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Table
@Data
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date entryDate;
    private Date exitDate;
    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;
}