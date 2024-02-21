package com.murbanowicz.interviewtask.api.controllers;

import com.murbanowicz.interviewtask.api.dto.response.ParentStatementResponse;
import com.murbanowicz.interviewtask.api.dto.response.SchoolStatementResponse;
import com.murbanowicz.interviewtask.service.StatementsService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statements")
@RequiredArgsConstructor
public class StatementsController {

    private final StatementsService statementsService;

    @GetMapping("/school/{schoolId}")
    public SchoolStatementResponse getSchoolMonthlyStatement(
            @PathVariable Long schoolId,
            @RequestParam @Min(2024) int year,
            @RequestParam @Range(min = 1, max = 12) int month
    ) {
        return statementsService.getStatementForSchool(schoolId, year, month);
    }

    @GetMapping("/school/{schoolId}/parent/{parentId}")
    public ParentStatementResponse getParentMonthlyStatement(
            @PathVariable Long schoolId,
            @PathVariable Long parentId,
            @RequestParam @Min(2024) int year,
            @RequestParam @Range(min = 1, max = 12) int month
    ) {
        return statementsService.getStatementForParent(schoolId, parentId, year, month);
    }
}