package com.murbanowicz.interviewtask.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Parent {

    private String firstName;
    private String lastName;
}