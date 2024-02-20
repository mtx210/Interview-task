package com.murbanowicz.interviewtask.api.data.repository;

import com.murbanowicz.interviewtask.api.data.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepository extends JpaRepository<Child, Long> {

}