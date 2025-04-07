package com.shiftm.shiftm.domain.leaverequest.dto;

import com.shiftm.shiftm.domain.leaverequest.dto.request.RequestLeaveRequest;

import java.time.LocalDate;

public class RequestLeaveRequestBuilder {
    public static RequestLeaveRequest build(final LocalDate startDate, final LocalDate endDate) {
        final Long leaveTypeId = 1L;
        final int hour = 8;

        return new RequestLeaveRequest(leaveTypeId, startDate, endDate, hour, null, null);
    }
}