package com.murbanowicz.interviewtask.data.repository;

import com.murbanowicz.interviewtask.data.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findAttendanceByChildIdAndEntryDateBetween(
            Long childId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}