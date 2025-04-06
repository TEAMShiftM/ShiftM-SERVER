package com.shiftm.shiftm.domain.leaverequest.dto;

import com.shiftm.shiftm.domain.leaverequest.dto.request.LeaveRequestPeriod;
import com.shiftm.shiftm.domain.leaverequest.dto.request.RequestLeaveRequest;

import java.util.List;

public class RequestLeaveRequestBuilder {
    public static RequestLeaveRequest build(final List<LeaveRequestPeriod> periodList) {
        return new RequestLeaveRequest(null, periodList);
    }
}