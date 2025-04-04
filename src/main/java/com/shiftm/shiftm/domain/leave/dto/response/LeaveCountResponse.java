package com.shiftm.shiftm.domain.leave.dto.response;

import com.querydsl.core.Tuple;

public record LeaveCountResponse(
        Double count,
        Double usableCount
) {
    public static LeaveCountResponse of(final Tuple leaveTuple) {
        if (leaveTuple == null) {
            return new LeaveCountResponse(null, null);
        }

        return new LeaveCountResponse(leaveTuple.get(0, Double.class), leaveTuple.get(1, Double.class));
    }
}