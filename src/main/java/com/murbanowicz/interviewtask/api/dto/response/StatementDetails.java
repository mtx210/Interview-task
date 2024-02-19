package com.murbanowicz.interviewtask.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StatementDetails {

    private String childName;
    private String childLastName;
    private String parentName;
    private String parentLastName;
    private List<SchoolTime> schoolTime;
}