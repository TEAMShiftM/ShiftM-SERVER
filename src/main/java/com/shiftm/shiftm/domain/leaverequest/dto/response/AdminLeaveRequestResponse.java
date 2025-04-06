package com.shiftm.shiftm.domain.leaverequest.dto.response;

import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record AdminLeaveRequestResponse(
        @Schema(description = "연차 요청 id")
        Long leaveRequestId,
        @Schema(description = "연차 유형")
        String LeaveTypeName,
        @Schema(description = "직원 id")
        String memberId,
        @Schema(description = "직원 이름")
        String memberName,
        @Schema(description = "요청 연차의 시작일")
        LocalDate startDate,
        @Schema(description = "요청 연차의 종료일")
        LocalDate endDate,
        @Schema(description = "연차 전체 수")
        Double count,
        @Schema(description = "연차 사용 수")
        Double usedCount,
        @Schema(description = "연차 요청 일수")
        Double requestCount,
        @Schema(description = "연차 요청 상태")
        Status status
) {
    public AdminLeaveRequestResponse(final LeaveRequest leaveRequest) {
        this(leaveRequest.getId(), leaveRequest.getLeave().getLeaveType().getName(), leaveRequest.getMember().getId(),
                leaveRequest.getMember().getName(), leaveRequest.getStartDate(), leaveRequest.getEndDate(),
                leaveRequest.getLeave().getCount(), leaveRequest.getLeave().getUsedCount(), leaveRequest.getCount(),
                leaveRequest.getStatus());
    }
}