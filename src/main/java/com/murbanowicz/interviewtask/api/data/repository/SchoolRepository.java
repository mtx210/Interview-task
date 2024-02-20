package com.murbanowicz.interviewtask.api.data.repository;

import com.murbanowicz.interviewtask.api.data.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {

}