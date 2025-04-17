package com.shiftm.shiftm.domain.objection.dto.request;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.objection.domain.Objection;
import com.shiftm.shiftm.domain.objection.domain.enums.ShiftType;
import com.shiftm.shiftm.domain.objection.domain.enums.Status;
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
        public Objection toEntity(final Member member) {
                return Objection.builder()
                        .targetShiftId(targetShiftId)
                        .shiftType(ShiftType.valueOf(shiftType.toUpperCase()))
                        .updatedTime(updatedTime)
                        .status(Status.PENDING)
                        .member(member)
                        .build();
        }
}
