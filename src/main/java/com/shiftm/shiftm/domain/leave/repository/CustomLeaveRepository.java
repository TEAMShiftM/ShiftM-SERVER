package com.shiftm.shiftm.domain.leave.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shiftm.shiftm.domain.leave.domain.Leave;
import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import com.shiftm.shiftm.domain.leave.domain.QLeave;
import com.shiftm.shiftm.domain.leave.domain.QLeaveType;
import com.shiftm.shiftm.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class CustomLeaveRepository {

    private final JPAQueryFactory queryFactory;
    private final QLeave qLeave = QLeave.leave;
    private final QLeaveType qLeaveType = QLeaveType.leaveType;

    public boolean existsValidLeaveForLeaveType(final Long leaveTypeId, final LocalDate date) {
        return queryFactory.selectOne()
                .from(qLeave)
                .where(
                        qLeave.leaveType.id.eq(leaveTypeId),
                        qLeave.expirationDate.goe(date),
                        qLeave.count.gt(qLeave.usedCount)
                )
                .fetchFirst() != null;
    }

    public Page<Leave> findLeaveByMember(final Member member, final Pageable pageable) {
        final NumberExpression<Integer> orderBy = new CaseBuilder()
                .when(qLeave.expirationDate.goe(LocalDate.now()).and(qLeave.count.gt(qLeave.usedCount))).then(1)
                .when(qLeave.expirationDate.goe(LocalDate.now()).and(qLeave.count.eq(qLeave.usedCount))).then(2)
                .otherwise(3);

        final List<Leave> content = queryFactory.selectFrom(qLeave)
                .join(qLeave.leaveType, qLeaveType).fetchJoin()
                .where(
                        qLeave.member.eq(member)
                )
                .orderBy(orderBy.asc(), qLeave.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        final Long count = queryFactory.select(qLeave.count())
                .from(qLeave)
                .where(
                        qLeave.member.eq(member)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    public Page<Leave> findAll(final Pageable pageable) {
        final List<Leave> content = queryFactory.selectFrom(qLeave)
                .join(qLeave.leaveType, qLeaveType).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qLeave.id.desc())
                .fetch();

        final Long count = queryFactory.select(qLeave.count())
                .from(qLeave)
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    public Tuple findByMemberIdAndLeaveTypeAndExpirationDate(final String memberId, final LeaveType leaveType,
                                                             final LocalDate date) {
        return queryFactory.select(qLeave.count.sum(), qLeave.count.subtract(qLeave.usedCount).sum())
                .from(qLeave)
                .where(qLeave.member.id.eq(memberId),
                        qLeave.leaveType.eq(leaveType),
                        qLeave.count.ne(qLeave.usedCount),
                        qLeave.expirationDate.goe(date))
                .fetchOne();
    }
}