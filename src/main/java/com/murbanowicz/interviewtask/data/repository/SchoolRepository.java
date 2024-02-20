package com.murbanowicz.interviewtask.data.repository;

import com.murbanowicz.interviewtask.data.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {

}