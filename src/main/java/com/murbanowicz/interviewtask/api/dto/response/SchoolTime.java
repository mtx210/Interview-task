package com.murbanowicz.interviewtask.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Duration;

@Data
@Builder
public class SchoolTime {

    private Date date;
    private Duration paidTimeInSchool;
    private BigDecimal paymentAmount;
}