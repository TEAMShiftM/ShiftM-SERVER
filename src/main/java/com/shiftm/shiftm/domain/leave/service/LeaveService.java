package com.shiftm.shiftm.domain.leave.service;

import com.querydsl.core.Tuple;
import com.shiftm.shiftm.domain.leave.domain.Leave;
import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import com.shiftm.shiftm.domain.leave.dto.request.CreateLeaveRequest;
import com.shiftm.shiftm.domain.leave.dto.request.UpdateLeaveRequest;
import com.shiftm.shiftm.domain.leave.repository.LeaveFindDao;
import com.shiftm.shiftm.domain.leave.repository.LeaveRepository;
import com.shiftm.shiftm.domain.leave.repository.LeaveTypeFindDao;
import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.exception.MemberNotFoundException;
import com.shiftm.shiftm.domain.member.repository.MemberFindDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LeaveService {

    private final LeaveFindDao leaveFindDao;
    private final MemberFindDao memberFindDao;
    private final LeaveTypeFindDao leaveTypeFindDao;
    private final LeaveRepository leaveRepository;

    @Transactional
    public List<Leave> createLeaves(final CreateLeaveRequest requestDto) {
        final List<Member> memberList = memberFindDao.findByIdIn(requestDto.memberIdList());

        validateMembers(requestDto.memberIdList(), memberList);

        final LeaveType leaveType = leaveTypeFindDao.findById(requestDto.leaveTypeId());

        final List<Leave> leaveList = memberList.stream()
                .map(member -> createLeave(member, toEntity(requestDto, leaveType)))
                .collect(Collectors.toList());

        return leaveRepository.saveAll(leaveList);
    }

    @Transactional
    public Leave updateLeave(final Long leaveId, final UpdateLeaveRequest requestDto) {
        final Leave leave = leaveFindDao.findById(leaveId);

        if (leave.getLeaveType().getId() != requestDto.leaveTypeId()) {
            final LeaveType leaveType = leaveTypeFindDao.findById(requestDto.leaveTypeId());

            leave.updateLeaveType(leaveType);
        }

        leave.updateLeave(requestDto.count(), requestDto.usedCount(), requestDto.expirationDate());

        return leave;
    }

    @Transactional(readOnly = true)
    public Tuple getLeaveCountByLeaveType(final String memberId, final Long leaveTypeId) {
        final LeaveType leaveType = leaveTypeFindDao.findById(leaveTypeId);

        return leaveFindDao.findLeaveCountByMemberIdAndLeaveTypeAndExpirationDateGreaterThanEqual(memberId, leaveType, LocalDate.now());
    }

    @Transactional(readOnly = true)
    public Page<Leave> getAllLeave(final int page, final int size) {
        final Pageable pageable = PageRequest.of(page, size);

        return leaveFindDao.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Leave> getLeaveByMember(final String memberId, final int page, final int size) {
        final Member member = memberFindDao.findById(memberId);

        final Pageable pageable = PageRequest.of(page, size);

        return leaveFindDao.findByMember(member, pageable);
    }

    private Leave toEntity(final CreateLeaveRequest requestDto, final LeaveType leaveType) {
        final Leave leave = requestDto.toEntity();

        leave.updateLeaveType(leaveType);

        return leave;
    }

    private Leave createLeave(final Member member, final Leave leave) {
        leave.updateMember(member);

        return leave;
    }

    private void validateMembers(final List<String> memberIdList, final List<Member> members) {
        if (memberIdList.size() != members.size()) {
            final List<String> validIds = members.stream()
                    .map(Member::getId)
                    .collect(Collectors.toList());

            memberIdList.stream()
                    .filter(id -> !validIds.contains(id))
                    .findAny()
                    .ifPresent(id -> {
                        throw new MemberNotFoundException(id);
                    });
        }
    }
}
