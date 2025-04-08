package com.shiftm.shiftm.domain.leaverequest.domain;

import com.shiftm.shiftm.domain.leaverequest.domain.enums.Status;

import java.time.LocalDate;

public class LeaveRequestBuilder {
    public static LeaveRequest build(final Status status) {
        final LocalDate startDate = LocalDate.of(2025, 4, 1);
        final LocalDate endDate = LocalDate.of(2025, 4, 2);
        final Double count = 2.0;

        return LeaveRequest.builder()
                .startDate(startDate)
                .endDate(endDate)
                .count(count)
                .status(status)
                .build();
    }
}