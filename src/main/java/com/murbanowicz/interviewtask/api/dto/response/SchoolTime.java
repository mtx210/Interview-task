package com.murbanowicz.interviewtask.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class SchoolTime {

    private LocalDate date;
    private int paidTimeInSchoolInHours;
    private BigDecimal paymentAmount;
}