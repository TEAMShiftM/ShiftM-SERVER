package com.shiftm.shiftm.domain.objection.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateObjectionReq(
        @NotNull
        Long shiftId,
        @NotBlank
        String type,
        @NotBlank
        String updatedType,
        @NotNull
        LocalDateTime updatedTime
) {
}
