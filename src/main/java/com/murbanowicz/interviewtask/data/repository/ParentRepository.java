package com.murbanowicz.interviewtask.data.repository;

import com.murbanowicz.interviewtask.data.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parent, Long> {

}