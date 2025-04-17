package com.shiftm.shiftm.domain.objection.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateObjectionReq(
        @NotNull
        Long targetShiftId,
        @NotBlank
        String shiftType,
        @NotNull
        LocalDateTime updatedTime
) {
}
