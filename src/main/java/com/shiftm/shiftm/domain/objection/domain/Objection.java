package com.shiftm.shiftm.domain.objection.domain;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.objection.domain.enums.Status;
import com.shiftm.shiftm.domain.objection.domain.enums.ShiftType;
import com.shiftm.shiftm.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "objections")
@Entity
public class Objection extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long targetShiftId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ShiftType shiftType;

    @Column(nullable = false)
    private LocalDateTime updatedTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Objection(final Long targetShiftId,
                     final ShiftType shiftType,
                     final LocalDateTime updatedTime,
                     final Status status,
                     final Member member) {
        this.targetShiftId = targetShiftId;
        this.shiftType = shiftType;
        this.updatedTime = updatedTime;
        this.status = status;
        this.member = member;
    }
}
