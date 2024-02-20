package com.murbanowicz.interviewtask.api.controllers;

import com.murbanowicz.interviewtask.api.dto.response.ParentStatementResponse;
import com.murbanowicz.interviewtask.api.dto.response.SchoolStatementResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Month;

@RestController("/api/statements")
public class StatementsController {

    @GetMapping("/school/{id}")
    public SchoolStatementResponse getSchoolMonthlyStatement(
            @PathVariable Long id,
            @RequestParam Month month
    ) {
        // todo
        return SchoolStatementResponse.builder().build();
    }

    @GetMapping("/school/{schoolId}/parent/{parentId}")
    public ParentStatementResponse getParentMonthlyStatement(
            @PathVariable Long schoolId,
            @PathVariable Long parentId,
            @RequestParam Month month
    ) {
        // todo
        return ParentStatementResponse.builder().build();
    }
}