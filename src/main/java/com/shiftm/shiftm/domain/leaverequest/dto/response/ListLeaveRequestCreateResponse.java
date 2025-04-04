package com.shiftm.shiftm.domain.leaverequest.dto.response;

import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;

import java.util.List;
import java.util.stream.Collectors;

public record ListLeaveRequestCreateResponse(
        List<LeaveRequestResponse> leaveRequestList
) {
    public static ListLeaveRequestCreateResponse of(final List<LeaveRequest> leaveRequestList) {
        final List<LeaveRequestResponse> leaveRequestResponseList = leaveRequestList.stream()
                .map(LeaveRequestResponse::new)
                .collect(Collectors.toList());

        return new ListLeaveRequestCreateResponse(leaveRequestResponseList);
    }
}