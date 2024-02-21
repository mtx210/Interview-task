package com.murbanowicz.interviewtask.api.controllers;

import com.murbanowicz.interviewtask.api.dto.response.ParentStatementResponse;
import com.murbanowicz.interviewtask.api.dto.response.SchoolStatementResponse;
import com.murbanowicz.interviewtask.service.StatementsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statements")
@RequiredArgsConstructor
public class StatementsController {

    private final StatementsService statementsService;

    @GetMapping("/school/{schoolId}")
    public SchoolStatementResponse getSchoolMonthlyStatement(
            @PathVariable Long schoolId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return statementsService.getStatementForSchool(schoolId, year, month);
    }

    @GetMapping("/school/{schoolId}/parent/{parentId}")
    public ParentStatementResponse getParentMonthlyStatement(
            @PathVariable Long schoolId,
            @PathVariable Long parentId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return statementsService.getStatementForParent(schoolId, parentId, year, month);
    }
}