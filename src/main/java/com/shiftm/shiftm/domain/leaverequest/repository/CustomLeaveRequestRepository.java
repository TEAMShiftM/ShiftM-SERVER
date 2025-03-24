package com.shiftm.shiftm.domain.leaverequest.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shiftm.shiftm.domain.leave.domain.QLeave;
import com.shiftm.shiftm.domain.leave.domain.QLeaveType;
import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.domain.QLeaveRequest;
import com.shiftm.shiftm.domain.member.domain.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CustomLeaveRequestRepository {

    private final JPAQueryFactory queryFactory;
    private final QLeaveRequest qLeaveRequest = QLeaveRequest.leaveRequest;

    public Page<LeaveRequest> findByMember(final String memberId, Pageable pageable) {
        final List<LeaveRequest> content = queryFactory.selectFrom(qLeaveRequest)
                .join(qLeaveRequest.member, QMember.member).fetchJoin()
                .join(qLeaveRequest.leave, QLeave.leave).fetchJoin()
                .join(qLeaveRequest.leave.leaveType, QLeaveType.leaveType).fetchJoin()
                .where(
                        qLeaveRequest.member.id.eq(memberId)
                )
                .orderBy(qLeaveRequest.id.desc())
                .fetch();

        final Long count = queryFactory.select(qLeaveRequest.count())
                .from(qLeaveRequest)
                .where(
                        qLeaveRequest.member.id.eq(memberId)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    public Page<LeaveRequest> findAll(Pageable pageable) {
        final List<LeaveRequest> content = queryFactory.selectFrom(qLeaveRequest)
                .join(qLeaveRequest.member, QMember.member).fetchJoin()
                .join(qLeaveRequest.leave, QLeave.leave).fetchJoin()
                .join(qLeaveRequest.leave.leaveType, QLeaveType.leaveType).fetchJoin()
                .orderBy(qLeaveRequest.id.desc())
                .fetch();

        final Long count = queryFactory.select(qLeaveRequest.count())
                .from(qLeaveRequest)
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }
}