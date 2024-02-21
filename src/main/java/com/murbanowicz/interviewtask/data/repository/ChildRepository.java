package com.murbanowicz.interviewtask.data.repository;

import com.murbanowicz.interviewtask.data.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChildRepository extends JpaRepository<Child, Long> {

    List<Child> findChildrenByParentIdAndSchoolId(Long parentId, Long schoolId);

    List<Child> findChildrenBySchoolId(Long schoolId);
}