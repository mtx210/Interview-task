package com.murbanowicz.interviewtask.data.repository;

import com.murbanowicz.interviewtask.data.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

}