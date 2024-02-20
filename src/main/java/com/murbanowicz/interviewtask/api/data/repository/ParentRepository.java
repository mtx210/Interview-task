package com.murbanowicz.interviewtask.api.data.repository;

import com.murbanowicz.interviewtask.api.data.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parent, Long> {

}