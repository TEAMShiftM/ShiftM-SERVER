package com.shiftm.shiftm.domain.leaverequest.dto.request;

import com.shiftm.shiftm.domain.leaverequest.domain.enums.Status;
import jakarta.validation.constraints.NotNull;

public record LeaveRequestStatusRequest(
        @NotNull
        Status status
) {
}