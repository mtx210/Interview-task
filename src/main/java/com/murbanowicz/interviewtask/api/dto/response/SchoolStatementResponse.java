package com.murbanowicz.interviewtask.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Data
@Builder
public class SchoolStatementResponse {

    private String schoolName;
    private YearMonth statementMonth;
    private BigDecimal statementAmountTotal;
    private List<StatementDetails> statementDetails;
}