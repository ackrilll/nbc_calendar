package com.sparta.calendar.repository;
import java.sql.*;
import java.time.LocalDate;
import com.sparta.calendar.dto.ScheduleRequestDto;
import com.sparta.calendar.dto.ScheduleResponseDto;
import com.sparta.calendar.entity.Schedule;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduleRepository {
    private final JdbcTemplate jdbcTemplate;
    public ScheduleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public Schedule save(Schedule schedule) {
        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO schedule (todo, charge,password,createDate,updateDate) VALUES (?, ?,?,?,?)";
        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, schedule.getContents());
                    preparedStatement.setString(2, schedule.getCharge());
                    preparedStatement.setString(3, schedule.getPassword());
                    preparedStatement.setTimestamp(4, Timestamp.valueOf(schedule.getCreatDate()));
                    preparedStatement.setTimestamp(5, Timestamp.valueOf(schedule.getUpdateDate()));
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        schedule.setId(id);

        return schedule;
    }
    public ScheduleResponseDto findScheduleById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM schedule WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                String charge = rs.getString("charge");
                String contents = rs.getString("todo"); //
                LocalDateTime updateDate = rs.getTimestamp("updateDate").toLocalDateTime();
                LocalDateTime createDate = rs.getTimestamp("createDate").toLocalDateTime();
                return new ScheduleResponseDto(id, charge, contents, updateDate, createDate);
            }
        });
    }


    public List<ScheduleResponseDto> findAll() {
        // DB 조회
        String sql = "SELECT * FROM schedule";

        return jdbcTemplate.query(sql, new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Schedule 데이터들을 ScheduleResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String charge = rs.getString("charge");
                String contents = rs.getString("todo"); //
                LocalDateTime createDate = rs.getTimestamp("createDate").toLocalDateTime();
                LocalDateTime updateDate = rs.getTimestamp("updateDate").toLocalDateTime();
                return new ScheduleResponseDto(id, charge, contents, createDate, updateDate);
            }
        });
    }
    public List<ScheduleResponseDto> findAll(String chargeOrUpdate, int quiryFlag) {
        //charge 기준인지, update날짜 기준인지 체크
        if (quiryFlag == 0) { // 담당자 이름 기준 조회
            // DB 조회
            System.out.println("담당자 이름 기준 조회");
            String sql = "SELECT * FROM schedule WHERE charge = ?";

            return jdbcTemplate.query(sql, new Object[]{chargeOrUpdate}, new RowMapper<ScheduleResponseDto>() {
                @Override
                public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Long id = rs.getLong("id");
                    String charge = rs.getString("charge");
                    String contents = rs.getString("todo");
                    LocalDateTime createDate = rs.getTimestamp("createDate").toLocalDateTime();
                    LocalDateTime updateDate = rs.getTimestamp("updateDate").toLocalDateTime();
                    return new ScheduleResponseDto(id, charge, contents, createDate, updateDate);
                }
            });
        } else { // 업데이트 날짜 기준 조회
            // DB 조회

            System.out.println("업데이트 날짜 기준 조회");
            String sql = "SELECT * FROM schedule WHERE DATE (updateDate)= ?";
            return jdbcTemplate.query(sql,new Object[]{chargeOrUpdate}, new RowMapper<ScheduleResponseDto>() {
                @Override
                public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                    // SQL 의 결과로 받아온 Schedule 데이터들을 ScheduleResponseDto 타입으로 변환해줄 메서드
                    Long id = rs.getLong("id");
                    String charge = rs.getString("charge");
                    String contents = rs.getString("todo"); //
                    LocalDateTime createDate = rs.getTimestamp("createDate").toLocalDateTime();
                    LocalDateTime updateDate = rs.getTimestamp("updateDate").toLocalDateTime();
                    return new ScheduleResponseDto(id, charge, contents, createDate, updateDate);
                }
            });
        }

    }
    public List<ScheduleResponseDto> findAll(String updateDate,String charge) {
        // DB 조회
        System.out.println("담당자, 업데이트 날짜 기준 조회");
        String sql = "SELECT * FROM schedule WHERE charge = ?AND DATE (updateDate)= ? ";

        return jdbcTemplate.query(sql,new Object[]{charge,updateDate}, new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Schedule 데이터들을 ScheduleResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String charge = rs.getString("charge");
                String contents = rs.getString("todo"); //
                LocalDateTime createDate = rs.getTimestamp("createDate").toLocalDateTime();
                LocalDateTime updateDate = rs.getTimestamp("updateDate").toLocalDateTime();
                return new ScheduleResponseDto(id, charge, contents, createDate, updateDate);
            }
        });
    }

    public void update(Long id, ScheduleRequestDto requestDto) {
        String sql = "UPDATE schedule SET charge = ?, todo = ?, password = ?, updateDate = ? WHERE id = ?";

        jdbcTemplate.update(sql, requestDto.getCharge(), requestDto.getContents(),requestDto.getPassword(),LocalDateTime.now(), id);

    }

    public void delete(Long id) {
        String sql = "DELETE FROM Schedule WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }


    public Schedule findByIdAndPassword(Long id, String password) {String sql = "SELECT * FROM Schedule WHERE id = ? AND password = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id, password}, (resultSet, rowNum) -> {
                Schedule schedule = new Schedule();
                schedule.setId(resultSet.getLong("id"));
                schedule.setContents(resultSet.getString("todo"));
                schedule.setCharge(resultSet.getString("charge"));
                schedule.setCreatDate(resultSet.getTimestamp("createDate").toLocalDateTime());
                schedule.setUpdateDate(resultSet.getTimestamp("updateDate").toLocalDateTime());
                return schedule;
            });
        } catch (EmptyResultDataAccessException e) {
            // 조건에 맞는 레코드가 없을 때 처리
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            // 조건에 맞는 레코드가 여러 개일 때 처리
            // 실제 사용 환경에서는 데이터 정합성을 검토해야 합니다.
            throw new IllegalStateException("Unexpected number of results found", e);
        }
    }
}
