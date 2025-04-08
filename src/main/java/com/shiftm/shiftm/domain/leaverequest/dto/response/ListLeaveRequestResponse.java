package com.shiftm.shiftm.domain.leaverequest.dto.response;

import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;

import java.util.List;
import java.util.stream.Collectors;

public record ListLeaveRequestResponse(
        List<LeaveRequestResponse> content,
        int page,
        int size,
        int totalPages,
        Long totalElements
) {
    public static ListLeaveRequestResponse of(final List<LeaveRequest> leaveRequestList, final int page, final int size,
                                              final int totalPages, final Long totalElements) {
        final List<LeaveRequestResponse> content = leaveRequestList.stream()
                .map(LeaveRequestResponse::new)
                .collect(Collectors.toList());

        return new ListLeaveRequestResponse(content, (leaveRequestList.isEmpty()) ? page : page + 1, size, totalPages,
                totalElements);
    }
}
