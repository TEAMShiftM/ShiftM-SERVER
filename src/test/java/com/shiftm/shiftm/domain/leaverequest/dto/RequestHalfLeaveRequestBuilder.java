package com.shiftm.shiftm.domain.leaverequest.dto;

import com.shiftm.shiftm.domain.leaverequest.dto.request.RequestLeaveRequest;

import java.time.LocalDate;
import java.time.LocalTime;

public class RequestHalfLeaveRequestBuilder {
    public static RequestLeaveRequest build() {
        final Long leaveTypeId = 1L;
        final LocalDate startDate = LocalDate.of(2025, 5, 2);
        final LocalDate endDate = LocalDate.of(2025, 5, 2);
        final int hour = 4;
        final LocalTime startTime = LocalTime.of(9, 0);
        final LocalTime endTime = LocalTime.of(13, 0);

        return new RequestLeaveRequest(leaveTypeId, startDate, endDate, hour, startTime, endTime);
    }
}