package com.shiftm.shiftm.domain.leaverequest.dto.response;

import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record LeaveRequestResponse(
        @Schema(description = "연차 요청 id")
        Long leaveRequestId,
        @Schema(description = "연차 유형")
        String LeaveTypeName,
        @Schema(description = "요청 연차의 시작일")
        LocalDate startDate,
        @Schema(description = "요청 연차의 종료일")
        LocalDate endDate,
        @Schema(description = "연차 요청 일수")
        Double count,
        @Schema(description = "연차 요청 상태")
        Status status
) {
    public LeaveRequestResponse(final LeaveRequest leaveRequest) {
        this(leaveRequest.getId(), leaveRequest.getLeave().getLeaveType().getName(), leaveRequest.getStartDate(),
                leaveRequest.getEndDate(), leaveRequest.getCount(), leaveRequest.getStatus());
    }
}
