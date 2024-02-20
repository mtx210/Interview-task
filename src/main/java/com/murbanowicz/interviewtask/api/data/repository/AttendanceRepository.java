package com.murbanowicz.interviewtask.api.data.repository;

import com.murbanowicz.interviewtask.api.data.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

}