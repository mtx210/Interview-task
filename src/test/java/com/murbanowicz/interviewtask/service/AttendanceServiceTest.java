package com.murbanowicz.interviewtask.service;

import com.murbanowicz.interviewtask.data.entity.Attendance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AttendanceServiceTest {

    @Autowired
    private AttendanceService attendanceService;

    @ParameterizedTest
    @MethodSource("mapToPaidHoursTestArgs")
    void mapToPaidHours(int expected, Attendance attendance) {
        assertEquals(expected, attendanceService.mapToPaidHours(attendance));
    }

    private static Stream<Arguments> mapToPaidHoursTestArgs() {
        return Stream.of(
                Arguments.of(
                        2,
                        Attendance.builder()
                                .entryDate(LocalDateTime.of(2024, 2, 2, 5, 30))
                                .exitDate(LocalDateTime.of(2024, 2, 2, 6, 30))
                                .build()
                ),
                Arguments.of(
                        2,
                        Attendance.builder()
                                .entryDate(LocalDateTime.of(2024, 2, 2, 5, 30))
                                .exitDate(LocalDateTime.of(2024, 2, 2, 9, 30))
                                .build()
                ),
                Arguments.of(
                        5,
                        Attendance.builder()
                                .entryDate(LocalDateTime.of(2024, 2, 2, 5, 30))
                                .exitDate(LocalDateTime.of(2024, 2, 2, 14, 30))
                                .build()
                ),
                Arguments.of(
                        0,
                        Attendance.builder()
                                .entryDate(LocalDateTime.of(2024, 2, 2, 9, 30))
                                .exitDate(LocalDateTime.of(2024, 2, 2, 11, 30))
                                .build()
                ),
                Arguments.of(
                        3,
                        Attendance.builder()
                                .entryDate(LocalDateTime.of(2024, 2, 2, 9, 30))
                                .exitDate(LocalDateTime.of(2024, 2, 2, 14, 30))
                                .build()
                ),
                Arguments.of(
                        3,
                        Attendance.builder()
                                .entryDate(LocalDateTime.of(2024, 2, 2, 13, 30))
                                .exitDate(LocalDateTime.of(2024, 2, 2, 15, 30))
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("validateAttendanceTestArgs")
    void validateAttendance(boolean expected, Attendance attendance) {
        assertEquals(expected, attendanceService.validateAttendance(attendance));
    }

    private static Stream<Arguments> validateAttendanceTestArgs() {
        return Stream.of(
                Arguments.of(
                        false,
                        Attendance.builder()
                                .entryDate(LocalDateTime.of(2024, 2, 1, 10, 30))
                                .exitDate(LocalDateTime.of(2024, 2, 2, 9, 30))
                                .build()),
                Arguments.of(
                        false,
                        Attendance.builder()
                                .entryDate(LocalDateTime.of(2024, 2, 1, 10, 30))
                                .exitDate(LocalDateTime.of(2024, 2, 1, 9, 30))
                                .build()),
                Arguments.of(
                        false,
                        Attendance.builder()
                                .entryDate(LocalDateTime.of(2024, 2, 1, 9, 30))
                                .exitDate(LocalDateTime.of(2024, 2, 2, 10, 30))
                                .build()),
                Arguments.of(
                        true,
                        Attendance.builder()
                                .entryDate(LocalDateTime.of(2024, 2, 1, 9, 30))
                                .exitDate(LocalDateTime.of(2024, 2, 1, 10, 30))
                                .build())
        );
    }
}