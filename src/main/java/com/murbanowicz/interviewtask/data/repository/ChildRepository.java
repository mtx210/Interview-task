package com.murbanowicz.interviewtask.data.repository;

import com.murbanowicz.interviewtask.data.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepository extends JpaRepository<Child, Long> {

}