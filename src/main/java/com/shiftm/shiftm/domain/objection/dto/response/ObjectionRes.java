package com.shiftm.shiftm.domain.objection.dto.response;

import com.shiftm.shiftm.domain.objection.domain.Objection;
import com.shiftm.shiftm.domain.objection.domain.enums.ShiftType;
import com.shiftm.shiftm.domain.objection.domain.enums.Status;

import java.time.LocalDateTime;

public record ObjectionRes(
        Long id,
        Long targetShiftId,
        ShiftType shiftType,
        LocalDateTime updatedTime,
        Status status
) {
    public ObjectionRes(final Objection objection) {
        this(objection.getId(), objection.getTargetShiftId(), objection.getShiftType(), objection.getUpdatedTime(), objection.getStatus());
    }
}
