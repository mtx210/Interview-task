package com.murbanowicz.interviewtask.api.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PaymentDetails {

    private String childName;
    private String childLastName;
    private List<SchoolTime> schoolTime;
}