package com.murbanowicz.interviewtask.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;

@Data
@Builder
public class SchoolStatementResponse {

    private String schoolName;
    private Month statementMonth;
    private BigDecimal statementAmountTotal;
    private List<StatementDetails> statementDetails;
}