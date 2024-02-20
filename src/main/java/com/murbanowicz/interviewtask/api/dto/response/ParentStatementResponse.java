package com.murbanowicz.interviewtask.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;

@Data
@Builder
public class ParentStatementResponse {

    private String schoolName;
    private Parent parent;
    private Month paymentMonth;
    private BigDecimal paymentAmountTotal;
    private List<PaymentDetails> paymentDetails;
}