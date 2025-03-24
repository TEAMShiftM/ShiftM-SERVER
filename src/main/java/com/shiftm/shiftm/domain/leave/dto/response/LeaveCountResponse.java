package com.shiftm.shiftm.domain.leave.dto.response;

import com.querydsl.core.Tuple;

public record LeaveCountResponse(
        Double count,
        Double usableCount
) {
    public LeaveCountResponse(final Tuple leaveTuple) {
        this(leaveTuple.get(0, Double.class), leaveTuple.get(1, Double.class));
    }
}