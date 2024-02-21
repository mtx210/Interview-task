package com.murbanowicz.interviewtask.service;

import com.murbanowicz.interviewtask.data.entity.Attendance;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class AttendanceService {

    private final LocalTime FREE_PERIOD_BEGIN_TIME = LocalTime.of(7,0,0);
    private final LocalTime FREE_PERIOD_END_TIME = LocalTime.of(12,0,0);

    // todo zmienic na iterator godzin w dobie ze sprawdzaniem czy godzina nalezy do darmowych ktore sa w propertiesach
    public int mapToPaidHours(Attendance attendance) {
        LocalTime entryTime = attendance.getEntryDate().toLocalTime();
        LocalTime exitTime = attendance.getExitDate().toLocalTime();

        if (entryTime.isBefore(FREE_PERIOD_BEGIN_TIME)) {
            if (exitTime.isBefore(FREE_PERIOD_BEGIN_TIME)) {
                return calculateHoursInMorningPeriod(entryTime, exitTime);
            } else if (exitTime.isBefore(FREE_PERIOD_END_TIME)) {
                return calculateHoursInMorningPeriod(entryTime, FREE_PERIOD_BEGIN_TIME.minusMinutes(1L));
            } else {
                return calculateHoursInMorningPeriod(entryTime, FREE_PERIOD_BEGIN_TIME.minusMinutes(1L))
                        + calculateHoursInMorningPeriod(FREE_PERIOD_END_TIME.plusMinutes(1L), exitTime);
            }
        } else if (entryTime.isBefore(FREE_PERIOD_END_TIME)) {
            if (exitTime.isBefore(FREE_PERIOD_END_TIME)) {
                return 0;
            } else {
                return calculateHoursInMorningPeriod(FREE_PERIOD_END_TIME.plusMinutes(1L), exitTime);
            }
        } else {
            return calculateHoursInMorningPeriod(entryTime, exitTime);
        }
    }

    private int calculateHoursInMorningPeriod(LocalTime entryTime, LocalTime exitTime) {
        return exitTime.getHour() - entryTime.getHour() + 1;
    }

    public boolean validateAttendance(Attendance attendance) {
        return attendance.getEntryDate().isBefore(attendance.getExitDate()) &&
                attendance.getExitDate().getDayOfYear() == attendance.getEntryDate().getDayOfYear();
    }
}