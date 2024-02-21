package com.murbanowicz.interviewtask.service;

import com.murbanowicz.interviewtask.api.dto.response.*;
import com.murbanowicz.interviewtask.data.entity.Attendance;
import com.murbanowicz.interviewtask.data.entity.Child;
import com.murbanowicz.interviewtask.data.entity.Parent;
import com.murbanowicz.interviewtask.data.entity.School;
import com.murbanowicz.interviewtask.data.repository.AttendanceRepository;
import com.murbanowicz.interviewtask.data.repository.ChildRepository;
import com.murbanowicz.interviewtask.data.repository.ParentRepository;
import com.murbanowicz.interviewtask.data.repository.SchoolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatementsServiceTest {

    @InjectMocks
    private StatementsService statementsService;
    @Mock
    private ParentRepository parentRepository;
    @Mock
    private SchoolRepository schoolRepository;
    @Mock
    private ChildRepository childRepository;
    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private AttendanceService attendanceService;

    @Test
    void getStatementForSchool() {
        when(schoolRepository.findById(any()))
                .thenReturn(Optional.of(School.builder()
                        .name("SP1")
                        .hourPrice(BigDecimal.valueOf(12.50))
                        .build()));

        when(childRepository.findChildrenBySchoolId(any()))
                .thenReturn(List.of(
                        Child.builder()
                                .firstName("Adam")
                                .lastName("Adamski")
                                .parent(Parent.builder()
                                        .firstName("Pawel")
                                        .lastName("Pawlowski")
                                        .build())
                                .build()));

        when(attendanceRepository.findAttendanceByChildIdAndEntryDateBetween(any(), any(), any()))
                .thenReturn(List.of(
                        Attendance.builder()
                                .entryDate(LocalDateTime.of(
                                        2024,
                                        2,
                                        2,
                                        5,
                                        30))
                                .exitDate(LocalDateTime.of(
                                        2024,
                                        2,
                                        2,
                                        14,
                                        30))
                                .build(),
                        Attendance.builder()
                                .entryDate(LocalDateTime.of(
                                        2024,
                                        2,
                                        3,
                                        6,
                                        30))
                                .exitDate(LocalDateTime.of(
                                        2024,
                                        2,
                                        3,
                                        11,
                                        30))
                                .build()
                ));

        when(attendanceService.validateAttendance(any()))
                .thenReturn(true);

        when(attendanceService.mapToPaidHours(any()))
                .thenReturn(5)
                .thenReturn(1);

        SchoolStatementResponse statementForSchool = statementsService.getStatementForSchool(1L, 2024, 2);

        assertEquals("SP1", statementForSchool.getSchoolName());
        assertEquals(YearMonth.of(2024, 2), statementForSchool.getStatementMonth());
        assertEquals(BigDecimal.valueOf(75.0), statementForSchool.getStatementAmountTotal());
        assertEquals(1, statementForSchool.getStatementDetails().size());

        StatementDetails statementDetailsElement = statementForSchool.getStatementDetails().get(0);
        assertEquals("Adam", statementDetailsElement.getChildName());
        assertEquals("Adamski", statementDetailsElement.getChildLastName());
        assertEquals("Pawel", statementDetailsElement.getParentName());
        assertEquals("Pawlowski", statementDetailsElement.getParentLastName());
        assertEquals(2, statementDetailsElement.getSchoolTime().size());

        SchoolTime schoolTime1 = statementDetailsElement.getSchoolTime().get(0);
        SchoolTime schoolTime2 = statementDetailsElement.getSchoolTime().get(1);

        assertSchoolTime(schoolTime1, LocalDate.of(2024, 2, 2), 5, BigDecimal.valueOf(62.5));
        assertSchoolTime(schoolTime2, LocalDate.of(2024, 2, 3), 1, BigDecimal.valueOf(12.5));
    }

    @Test
    void getStatementForParent() {
        when(schoolRepository.findById(any()))
                .thenReturn(Optional.of(School.builder()
                        .name("SP2")
                        .hourPrice(BigDecimal.valueOf(2.50))
                        .build()));

        when(parentRepository.findById(any()))
                .thenReturn(Optional.of(Parent.builder()
                        .firstName("Jacek")
                        .lastName("Jackowski")
                        .build()));

        when(childRepository.findChildrenByParentIdAndSchoolId(any(), any()))
                .thenReturn(List.of(
                        Child.builder()
                                .firstName("Jan")
                                .lastName("Janowski")
                                .build()));

        when(attendanceRepository.findAttendanceByChildIdAndEntryDateBetween(any(), any(), any()))
                .thenReturn(List.of(
                        Attendance.builder()
                                .entryDate(LocalDateTime.of(
                                        2024,
                                        2,
                                        5,
                                        8,
                                        30))
                                .exitDate(LocalDateTime.of(
                                        2024,
                                        2,
                                        5,
                                        11,
                                        30))
                                .build(),
                        Attendance.builder()
                                .entryDate(LocalDateTime.of(
                                        2024,
                                        2,
                                        6,
                                        5,
                                        30))
                                .exitDate(LocalDateTime.of(
                                        2024,
                                        2,
                                        6,
                                        16,
                                        30))
                                .build()
                ));

        when(attendanceService.validateAttendance(any()))
                .thenReturn(true);

        when(attendanceService.mapToPaidHours(any()))
                .thenReturn(0)
                .thenReturn(7);

        ParentStatementResponse statementForParent = statementsService.getStatementForParent(1L, 1L, 2024, 2);

        assertEquals("SP2", statementForParent.getSchoolName());
        assertEquals("Jacek", statementForParent.getParent().getFirstName());
        assertEquals("Jackowski", statementForParent.getParent().getLastName());
        assertEquals(YearMonth.of(2024, 2), statementForParent.getPaymentMonth());
        assertEquals(BigDecimal.valueOf(17.50), statementForParent.getPaymentAmountTotal());
        assertEquals(1, statementForParent.getPaymentDetails().size());

        PaymentDetails paymentDetails1 = statementForParent.getPaymentDetails().get(0);
        assertEquals("Jan", paymentDetails1.getChildName());
        assertEquals("Janowski", paymentDetails1.getChildLastName());
        assertEquals(2, paymentDetails1.getSchoolTime().size());

        SchoolTime schoolTime1 = paymentDetails1.getSchoolTime().get(0);
        SchoolTime schoolTime2 = paymentDetails1.getSchoolTime().get(1);

        assertSchoolTime(schoolTime1, LocalDate.of(2024, 2, 5), 0, BigDecimal.valueOf(0.0));
        assertSchoolTime(schoolTime2, LocalDate.of(2024, 2, 6), 7, BigDecimal.valueOf(17.50));
    }

    private void assertSchoolTime(SchoolTime schoolTime, LocalDate date, int paidTimeInSchoolInHours, BigDecimal paymentAmount) {
        assertEquals(date, schoolTime.getDate());
        assertEquals(paidTimeInSchoolInHours, schoolTime.getPaidTimeInSchoolInHours());
        assertEquals(paymentAmount, schoolTime.getPaymentAmount());
    }
}