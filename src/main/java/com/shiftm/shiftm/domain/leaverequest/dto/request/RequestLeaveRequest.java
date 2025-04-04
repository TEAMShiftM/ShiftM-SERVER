package com.shiftm.shiftm.domain.leaverequest.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RequestLeaveRequest(
        @NotNull
        Long leaveTypeId,
        @Valid @Size(min = 1)
        List<LeaveRequestPeriod> periodList
) {
}
