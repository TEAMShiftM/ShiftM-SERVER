package com.shiftm.shiftm.domain.leaverequest.dto;

import com.shiftm.shiftm.domain.leaverequest.domain.enums.Status;
import com.shiftm.shiftm.domain.leaverequest.dto.request.LeaveRequestStatusRequest;

public class LeaveRequestStatusRequestBuilder {
    public static LeaveRequestStatusRequest build(final Status status) {
        return new LeaveRequestStatusRequest(status);
    }
}