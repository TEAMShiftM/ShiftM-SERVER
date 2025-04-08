package com.shiftm.shiftm.domain.leaverequest.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record LeaveRequestPeriod(
        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate endDate,
        @NotNull @Min(2) @Max(8)
        int hour,
        LocalTime startTime,
        LocalTime endTime
) {
}