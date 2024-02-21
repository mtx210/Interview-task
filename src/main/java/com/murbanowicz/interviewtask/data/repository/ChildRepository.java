package com.murbanowicz.interviewtask.data.repository;

import com.murbanowicz.interviewtask.data.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ChildRepository extends JpaRepository<Child, Long> {

    @Query("SELECT c FROM Child c " +
            "JOIN FETCH c.attendances a " +
            "WHERE c.parent.id = :parentId " +
            "AND c.school.id = :schoolId " +
            "AND a.entryDate BETWEEN :startDate AND :endDate")
    List<Child> findChildrenWithAttendancesByParentIdAndSchoolId(
            @Param("parentId") Long parentId,
            @Param("schoolId") Long schoolId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT c FROM Child c " +
            "JOIN FETCH c.attendances a " +
            "WHERE c.school.id = :schoolId " +
            "AND a.entryDate BETWEEN :startDate AND :endDate")
    List<Child> findChildrenWithAttendancesSchoolId(
            @Param("schoolId") Long schoolId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}