package com.shiftm.shiftm.domain.leaverequest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record RequestLeaveRequest(
        @NotNull
        @Schema(description = "연차 유형 ID", defaultValue = "1")
        Long leaveTypeId,
        @NotNull
        @Schema(description = "연차 시작일")
        LocalDate startDate,
        @NotNull
        @Schema(description = "연차 종료일")
        LocalDate endDate,
        @Schema(description = "연차 사용 시간 (단위: 시간) - 2: 반반차, 4: 반차, 8: 연차")
        @NotNull @Min(2) @Max(8)
        int hour,
        @Schema(description = "반차 또는 반반차 시작 시각", defaultValue = "09:00:00")
        LocalTime startTime,
        @Schema(description = "반차 또는 반반차 종료 시각", defaultValue = "09:00:00")
        LocalTime endTime
) {
}
