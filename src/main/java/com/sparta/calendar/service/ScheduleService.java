package com.sparta.calendar.service;

import com.sparta.calendar.dto.ScheduleRequestDto;
import com.sparta.calendar.dto.ScheduleResponseDto;
import com.sparta.calendar.entity.Schedule;
import com.sparta.calendar.repository.ScheduleRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduleService {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {
        // RequestDto -> Entity
        Schedule schedule = new Schedule(requestDto);
        LocalDateTime now = LocalDateTime.now();
        schedule.setCreatDate(now);
        schedule.setUpdateDate(now);

        // DB 저장
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        Schedule saveSchedule = scheduleRepository.save(schedule);

        // Entity -> ResponseDto
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(saveSchedule);

        return scheduleResponseDto;
    }
    public ScheduleResponseDto getSchedule(Long id) {
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        return scheduleRepository.findScheduleById(id);
    }
    public List<ScheduleResponseDto> getSchedules() {
        // DB 조회
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        return scheduleRepository.findAll();
    }
    public List<ScheduleResponseDto> getSchedules( String chargeOrUpdate, int quiryFlag) {
        // DB 조회
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        if(quiryFlag == 0){ // 담당자 기준 조회
            System.out.println("ScheduleService 조건문 들어옴");
            return scheduleRepository.findAll(chargeOrUpdate,0);
        } else { // 수정일 기준 조회
            return scheduleRepository.findAll(chargeOrUpdate,1);
        }
    }
    public List<ScheduleResponseDto> getSchedules(String updateDate,String charge) {
        // DB 조회
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        return scheduleRepository.findAll(updateDate,charge);
    }

    public Long updateSchedule(Long id,String password, ScheduleRequestDto requestDto) {
        System.out.println("서비스 updateSchedule메서드 들어왔어");
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        // 해당 Schedule이 존재하는지 확인
        Schedule schedule = scheduleRepository.findByIdAndPassword(id,password);
        if (schedule != null) {
            // Schedule 내용 수정
            scheduleRepository.update(id, requestDto);
            System.out.println("스케쥴 찾았어");
            return id;
        } else {
            throw new IllegalArgumentException("선택한 스케쥴은 존재하지 않습니다.");
        }
    }
    /*
    public Long deleteSchedule(Long id) {
        ScheduleRepository scheduleRepository = new ScheduleRepository(jdbcTemplate);
        // 해당 메모가 DB에 존재하는지 확인
        Schedule schedule = scheduleRepository.findById(id);
        if (schedule != null) {
            // Schedule 삭제
            scheduleRepository.delete(id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 스케쥴은 존재하지 않습니다.");
        }
    }*/
    
}
