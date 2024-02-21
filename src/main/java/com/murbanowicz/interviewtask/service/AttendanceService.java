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

    public boolean validateAttendance(Attendance attendance) {
        return attendance.getEntryDate().isBefore(attendance.getExitDate()) &&
                attendance.getExitDate().getDayOfYear() == attendance.getEntryDate().getDayOfYear();
    }
}