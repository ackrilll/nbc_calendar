package com.sparta.calendar.dto;

import lombok.Getter;

@Getter
// 내용을 contents, charge, password 세 가지를 받는다
public class ScheduleRequestDto {
    private String contents;
    private String charge;
    private String password;
}
