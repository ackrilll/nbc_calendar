package com.sparta.calendar.controller;
import com.sparta.calendar.dto.ScheduleResponseDto;
import com.sparta.calendar.dto.ScheduleRequestDto;
import com.sparta.calendar.dto.ScheduleResponseDto;
import com.sparta.calendar.entity.Schedule;
import com.sparta.calendar.service.ScheduleService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ScheduleController {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/schedules")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        ScheduleService scheduleService = new ScheduleService(jdbcTemplate);
        return scheduleService.createSchedule(requestDto);
    }

    @GetMapping("/schedule")
    public ScheduleResponseDto getSchedule(@RequestParam Long id) {
        ScheduleService scheduleService = new ScheduleService(jdbcTemplate);
        return scheduleService.getSchedule(id);
    }

    @GetMapping("/schedules")
    public List<ScheduleResponseDto> getSchedules(@RequestParam(required = false) String updateDate, @RequestParam(required = false) String charge) {
        // 빈 문자열을 null로 변환
        String convertedUpdateDate = (updateDate != null && updateDate.isEmpty()) ? null : updateDate;
        String convertedCharge = (charge != null && charge.isEmpty()) ? null : charge;
        ScheduleService scheduleService = new ScheduleService(jdbcTemplate);

        if (convertedUpdateDate == null && convertedCharge == null) { // 기준 없음
            System.out.println("기준 없음");
            return scheduleService.getSchedules();
        } else if (convertedUpdateDate == null) { // 담당자 이름 기준 조회
            System.out.println("담당자 이름 기준");
            return scheduleService.getSchedules(convertedCharge, 0);
        } else if (convertedCharge == null) { // 수정일 기준 조회
            System.out.println("업데이트 날짜 기준");
            return scheduleService.getSchedules(convertedUpdateDate, 1);
        } else { // 두 가지 모두 기준 조회
            System.out.println("담당자, 업데이트 날짜 기준");
            System.out.println(convertedUpdateDate);
            System.out.println(convertedCharge);
            return scheduleService.getSchedules(convertedUpdateDate, convertedCharge);
        }
    }

    @PutMapping("/schedules")
    public Long updateSchedule(
            @RequestParam Long id,
            @RequestParam String password,
            @RequestBody  ScheduleRequestDto requestDto) {
        ScheduleService scheduleService = new ScheduleService(jdbcTemplate);
        return scheduleService.updateSchedule(id,password, requestDto);
    }
/*
    @DeleteMapping("/Schedules/{id}")
    public Long deleteSchedule(@PathVariable Long id) {
        ScheduleService scheduleService = new ScheduleService(jdbcTemplate);
        return scheduleService.deleteSchedule(id);
    }
*/
}
