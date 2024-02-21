package com.murbanowicz.interviewtask.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Data
@Builder
public class ParentStatementResponse {

    private String schoolName;
    private Parent parent;
    private YearMonth paymentMonth;
    private BigDecimal paymentAmountTotal;
    private List<PaymentDetails> paymentDetails;
}