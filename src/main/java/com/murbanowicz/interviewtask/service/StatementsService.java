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
import com.murbanowicz.interviewtask.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatementsService {

    private final ParentRepository parentRepository;
    private final SchoolRepository schoolRepository;
    private final ChildRepository childRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceService attendanceService;

    public SchoolStatementResponse getStatementForSchool(Long schoolId, int year, int month) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new ResourceNotFoundException("School not found for id: " + schoolId));
        BigDecimal schoolHourRate = school.getHourPrice();
        YearMonth statementMonth = YearMonth.of(year, month);

        List<StatementDetails> statementDetails = buildStatementDetails(schoolId, statementMonth, schoolHourRate);

        return SchoolStatementResponse.builder()
                .schoolName(school.getName())
                .statementMonth(statementMonth)
                .statementAmountTotal(buildStatementAmountTotal(statementDetails))
                .statementDetails(statementDetails)
                .build();
    }

    private List<StatementDetails> buildStatementDetails(Long schoolId, YearMonth statementMonth,
                                                         BigDecimal schoolHourRate
    ) {
        LocalDateTime monthStart = statementMonth.atDay(1).atStartOfDay();
        LocalDateTime monthEnd = statementMonth.atEndOfMonth().atTime(23,59,59);

        return childRepository.findChildrenBySchoolId(schoolId).stream()
                .map(child -> StatementDetails.builder()
                        .childName(child.getFirstName())
                        .childLastName(child.getLastName())
                        .parentName(child.getParent().getFirstName())
                        .parentLastName(child.getParent().getLastName())
                        .schoolTime(buildSchoolTime(child, monthStart, monthEnd, schoolHourRate))
                        .build())
                .toList();
    }

    private BigDecimal buildStatementAmountTotal(List<StatementDetails> statementDetails) {
        return statementDetails.stream()
                .flatMap(statementDetailsElement -> statementDetailsElement.getSchoolTime().stream())
                .map(SchoolTime::getPaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public ParentStatementResponse getStatementForParent(Long schoolId, Long parentId, int year, int month) {
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent not found for id: " + parentId));
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new ResourceNotFoundException("School not found for id: " + schoolId));
        BigDecimal schoolHourRate = school.getHourPrice();
        YearMonth statementMonth = YearMonth.of(year, month);

        List<PaymentDetails> paymentDetails = buildPaymentDetails(parentId, schoolId, statementMonth, schoolHourRate);

        return ParentStatementResponse.builder()
                .schoolName(school.getName())
                .parent(com.murbanowicz.interviewtask.api.dto.response.Parent.builder()
                        .firstName(parent.getFirstName())
                        .lastName(parent.getLastName())
                        .build())
                .paymentMonth(statementMonth)
                .paymentAmountTotal(buildPaymentAmountTotal(paymentDetails))
                .paymentDetails(paymentDetails)
                .build();
    }

    private List<PaymentDetails> buildPaymentDetails(Long parentId, Long schoolId,
                                                     YearMonth statementMonth, BigDecimal schoolHourRate
    ) {
        LocalDateTime monthStart = statementMonth.atDay(1).atStartOfDay();
        LocalDateTime monthEnd = statementMonth.atEndOfMonth().atTime(23,59,59);

        return childRepository.findChildrenByParentIdAndSchoolId(parentId, schoolId).stream()
                .map(child -> PaymentDetails.builder()
                        .childName(child.getFirstName())
                        .childLastName(child.getLastName())
                        .schoolTime(buildSchoolTime(child, monthStart, monthEnd, schoolHourRate))
                        .build())
                .toList();
    }

    private List<SchoolTime> buildSchoolTime(Child child, LocalDateTime monthStart,
                                             LocalDateTime monthEnd, BigDecimal schoolHourRate
    ) {
        return attendanceRepository.findAttendanceByChildIdAndEntryDateBetween(child.getId(), monthStart, monthEnd)
                .stream()
                .filter(attendanceService::validateAttendance)
                .map(attendance -> buildSchoolTimeElement(attendance, schoolHourRate))
                .toList();
    }

    private SchoolTime buildSchoolTimeElement(Attendance attendance, BigDecimal schoolHourRate) {
        final int paidHoursAmount = attendanceService.mapToPaidHours(attendance);

        return SchoolTime.builder()
                .date(attendance.getEntryDate().toLocalDate())
                .paidTimeInSchoolInHours(paidHoursAmount)
                .paymentAmount(BigDecimal.valueOf(paidHoursAmount).multiply(schoolHourRate))
                .build();
    }

    private BigDecimal buildPaymentAmountTotal(List<PaymentDetails> paymentDetails) {
        return paymentDetails.stream()
                .flatMap(paymentDetailsElement -> paymentDetailsElement.getSchoolTime().stream())
                .map(SchoolTime::getPaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}