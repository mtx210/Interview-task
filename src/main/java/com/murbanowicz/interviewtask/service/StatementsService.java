package com.murbanowicz.interviewtask.service;

import com.murbanowicz.interviewtask.api.dto.response.SchoolStatementResponse;
import com.murbanowicz.interviewtask.api.dto.response.SchoolTime;
import com.murbanowicz.interviewtask.data.entity.Attendance;
import com.murbanowicz.interviewtask.data.entity.Child;
import com.murbanowicz.interviewtask.data.entity.Parent;
import com.murbanowicz.interviewtask.data.entity.School;
import com.murbanowicz.interviewtask.data.repository.ChildRepository;
import com.murbanowicz.interviewtask.data.repository.ParentRepository;
import com.murbanowicz.interviewtask.data.repository.SchoolRepository;
import com.murbanowicz.interviewtask.api.dto.response.ParentStatementResponse;
import com.murbanowicz.interviewtask.api.dto.response.PaymentDetails;
import com.murbanowicz.interviewtask.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatementsService {

    private final ParentRepository parentRepository;
    private final SchoolRepository schoolRepository;
    private final ChildRepository childRepository;

    private final LocalTime FREE_PERIOD_BEGIN_TIME = LocalTime.of(7,0,0);
    private final LocalTime FREE_PERIOD_END_TIME = LocalTime.of(12,0,0);

    public SchoolStatementResponse getStatementForSchool(Long schoolId, Month month) {
        // todo
        return SchoolStatementResponse.builder().build();
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

    private List<PaymentDetails> buildPaymentDetails(
            Long parentId,
            Long schoolId,
            YearMonth statementMonth,
            BigDecimal schoolHourRate
    ) {
        LocalDateTime monthStart = statementMonth.atDay(1).atStartOfDay();
        LocalDateTime monthEnd = statementMonth.atEndOfMonth().atTime(23,59,59);

        List<Child> childrenWithAttendances = childRepository.findChildrenWithAttendancesByParentIdAndSchoolId(
                parentId,
                schoolId,
                monthStart,
                monthEnd
        );

        return childrenWithAttendances.stream()
                .map(child -> PaymentDetails.builder()
                        .childName(child.getFirstName())
                        .childLastName(child.getLastName())
                        .schoolTime(buildSchoolTime(child, schoolHourRate))
                        .build())
                .toList();
    }

    private List<SchoolTime> buildSchoolTime(Child child, BigDecimal schoolHourRate) {
        return child.getAttendances().stream()
                .filter(this::validateAttendance)
                .map(attendance -> buildSchoolTimeElement(attendance, schoolHourRate))
                .toList();
    }

    private SchoolTime buildSchoolTimeElement(Attendance attendance, BigDecimal schoolHourRate) {
        final int paidHoursAmount = mapToPaidHours(attendance);

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

    private boolean validateAttendance(Attendance attendance) {
        return attendance.getEntryDate().isBefore(attendance.getExitDate()) &&
                attendance.getExitDate().getDayOfYear() == attendance.getEntryDate().getDayOfYear();
    }

    // todo zmienic na iterator godzin w dobie ze sprawdzaniem czy godzina nalezy do darmowych ktore sa w propertiesach
    private int mapToPaidHours(Attendance attendance) {
        LocalTime entryTime = attendance.getEntryDate().toLocalTime();
        LocalTime exitTime = attendance.getExitDate().toLocalTime();

        if (entryTime.isBefore(FREE_PERIOD_BEGIN_TIME)) {
            if (exitTime.isBefore(FREE_PERIOD_BEGIN_TIME)) {
                return calculateHoursInMorningPeriod(entryTime, exitTime);
            } else if (exitTime.isBefore(FREE_PERIOD_END_TIME)) {
                return calculateHoursInMorningPeriod(entryTime, FREE_PERIOD_BEGIN_TIME);
            } else {
                return calculateHoursInMorningPeriod(entryTime, FREE_PERIOD_BEGIN_TIME)
                        + calculateHoursInAfternoonPeriod(FREE_PERIOD_END_TIME, exitTime);
            }
        } else if (entryTime.isBefore(FREE_PERIOD_END_TIME)) {
            if (exitTime.isBefore(FREE_PERIOD_END_TIME)) {
                return 0;
            } else {
                return calculateHoursInAfternoonPeriod(FREE_PERIOD_END_TIME, exitTime);
            }
        } else {
            return calculateHoursInAfternoonPeriod(entryTime, exitTime);
        }
    }

    // 5:00:00 - 5:59:59
    //
    // 4:00:30
    // 6:30:20
    //
    private int calculateHoursInMorningPeriod(LocalTime entryTime, LocalTime exitTime) {
        return exitTime.getHour() - entryTime.getHour() + 1;
    }

    // 12:01:00 - 13:00:59
    //
    // 12:00:30
    // 14:30:00
    //
    private int calculateHoursInAfternoonPeriod(LocalTime entryTime, LocalTime exitTime) {
        return calculateHoursInMorningPeriod(
                entryTime.plusMinutes(1),
                exitTime.plusMinutes(1)
        );
    }
}