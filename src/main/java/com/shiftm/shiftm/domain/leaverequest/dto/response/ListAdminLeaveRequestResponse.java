package com.shiftm.shiftm.domain.leaverequest.dto.response;

import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;

import java.util.List;
import java.util.stream.Collectors;

public record ListAdminLeaveRequestResponse(
        List<AdminLeaveRequestResponse> content,
        int page,
        int size,
        int totalPages,
        Long totalElements
) {
    public static ListAdminLeaveRequestResponse of(final List<LeaveRequest> leaveRequestList, final int page,
                                                   final int size, final int totalPages, final Long totalElements) {
        final List<AdminLeaveRequestResponse> content = leaveRequestList.stream()
                .map(AdminLeaveRequestResponse::new)
                .collect(Collectors.toList());

        return new ListAdminLeaveRequestResponse(content, (leaveRequestList.isEmpty()) ? page : page + 1, size, totalPages,
                totalElements);
    }
}