package com.sparta.calendar.entity;

import com.sparta.calendar.dto.ScheduleRequestDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Schedule {
    private Long id;
    private LocalDateTime creatDate;
    private LocalDateTime updateDate;
    private String contents;
    private String charge;
    private String password;

    public Schedule(ScheduleRequestDto scheduleRequestDto) {
        this.contents = scheduleRequestDto.getContents();
        this.charge = scheduleRequestDto.getCharge();
        this.password = scheduleRequestDto.getPassword();}

}
