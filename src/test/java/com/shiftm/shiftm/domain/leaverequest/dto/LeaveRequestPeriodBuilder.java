package com.shiftm.shiftm.domain.leaverequest.dto;

import com.shiftm.shiftm.domain.leaverequest.dto.request.LeaveRequestPeriod;

import java.time.LocalDate;
import java.time.LocalTime;

public class LeaveRequestPeriodBuilder {
    public static LeaveRequestPeriod build(final LocalDate startDate, final LocalDate endDate, final int hour,
                                           final LocalTime startTime, final LocalTime endTime) {
        return new LeaveRequestPeriod(startDate, endDate, hour, startTime, endTime);
    }
}