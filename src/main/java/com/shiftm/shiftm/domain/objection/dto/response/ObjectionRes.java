package com.shiftm.shiftm.domain.objection.dto.response;

import com.shiftm.shiftm.domain.objection.domain.Objection;
import com.shiftm.shiftm.domain.objection.domain.enums.Type;

import java.time.LocalDateTime;

public record ObjectionRes(
        Long id,
        Long shiftId,
        Type type,
        LocalDateTime updatedTime
) {
    public ObjectionRes(final Objection objection) {
        this(objection.getId(), objection.getShiftId(), objection.getType(), objection.getUpdatedTime());
    }
}
