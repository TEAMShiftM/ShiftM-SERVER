package com.shiftm.shiftm.domain.leaverequest.domain;

import com.shiftm.shiftm.domain.leave.domain.Leave;
import com.shiftm.shiftm.domain.leaverequest.domain.enums.Status;
import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "leave_requests")
@Entity
public class LeaveRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Double count;

    private LocalTime startTime;

    private LocalTime endTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_id")
    private Leave leave;

    @Builder
    public LeaveRequest(final LocalDate startDate, final LocalDate endDate, final LocalTime startTime,
            final LocalTime endTime, final Double count, final Status status) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.count = count;
        this.status = status;
    }

    public void updateMember(final Member member) {
        this.member = member;
    }

    public void updateLeave(final Leave leave) {
        this.leave = leave;
    }

    public void updateStatus(final Status status) {
        this.status = status;
    }
}
