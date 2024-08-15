package com.sparta.calendar.dto;

import com.sparta.calendar.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {
    private Long scheduleId;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String contents;
    private String charge;

    public ScheduleResponseDto(Schedule schedule) {
        this.scheduleId = schedule.getId();
        this.createDate = schedule.getCreatDate();
        this.updateDate = schedule.getUpdateDate();
        this.contents = schedule.getContents();
        this.charge = schedule.getCharge();
    }

    //수정 시 사용될 생성자
    public ScheduleResponseDto(Long id, String charge, String contents, LocalDateTime updateDate, LocalDateTime createDate) {
        this.scheduleId = id;
        this.contents = contents;
        this.charge = charge;
        this.updateDate = updateDate;
        this.createDate = createDate;
    }
}
