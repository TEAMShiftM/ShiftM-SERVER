package com.shiftm.shiftm.domain.leaverequest.service;

import com.shiftm.shiftm.domain.leave.domain.Leave;
import com.shiftm.shiftm.domain.leave.repository.LeaveRepository;
import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.domain.enums.Status;
import com.shiftm.shiftm.domain.leaverequest.dto.request.RequestLeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.dto.request.LeaveRequestStatusRequest;
import com.shiftm.shiftm.domain.leaverequest.exception.*;
import com.shiftm.shiftm.domain.leaverequest.repository.LeaveRequestFindDao;
import com.shiftm.shiftm.domain.leaverequest.repository.LeaveRequestRepository;
import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.repository.MemberFindDao;
import com.shiftm.shiftm.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LeaveRequestService {

    private final LeaveRequestFindDao leaveRequestFindDao;
    private final MemberFindDao memberFindDao;
    private final LeaveRepository leaveRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    @Transactional
    public void requestLeave(final String memberId, final RequestLeaveRequest requestDto) {
        final Member member = memberFindDao.findById(memberId);

        final List<Leave> leaves = leaveRepository.findLeaves(memberId, requestDto.leaveTypeId(), LocalDate.now());

        final Double usableCount = leaves.stream()
                .mapToDouble(leave -> leave.getCount() - leave.getUsedCount())
                .sum();

        if (requestDto.count() > usableCount) {
            throw new LeaveNotEnoughException(ErrorCode.LEAVE_NOT_ENOUGH);
        }

        final List<LeaveRequest> leaveRequests = new ArrayList<>();

        Double count = requestDto.count();

        for (Leave leave : leaves) {
            if (requestDto.count() <= 0) {
                break;
            }

            double available = leave.getCount() - leave.getUsedCount();

            LeaveRequest leaveRequest;

            if (available >= count) {
                leaveRequest = new LeaveRequest(requestDto.startDate(), requestDto.endDate(), count, Status.PENDING);
                leaveRequest.updateLeave(leave);
                leaveRequest.updateMember(member);
                leaveRequests.add(leaveRequest);
                break;
            } else {
                leaveRequest = new LeaveRequest(requestDto.startDate(), requestDto.endDate(), available, Status.PENDING);
                leaveRequest.updateLeave(leave);
                leaveRequest.updateMember(member);
                leaveRequests.add(leaveRequest);
                count -= available;
            }
        }

        leaveRequestRepository.saveAll(leaveRequests);
    }

    @Transactional(readOnly = true)
    public Page<LeaveRequest> getLeaveRequestByMember(final String memberId, final int page, final int size) {
        final Pageable pageable = PageRequest.of(page, size);

        return leaveRequestFindDao.findByMember(memberId, pageable);
    }

    @Transactional
    public LeaveRequest cancelLeaveRequest(final String memberId, final Long leaveRequestId,
                                           final LeaveRequestStatusRequest request) {
        final Member member = memberFindDao.findById(memberId);

        final LeaveRequest leaveRequest = leaveRequestFindDao.findById(leaveRequestId);

        if (member != leaveRequest.getMember()) {
            throw new LeaveRequestNotAuthorException(ErrorCode.LEAVE_REQUEST_NOT_AUTHOR);
        }

        if (leaveRequest.getStatus() != Status.PENDING) {
            throw new StatusAlreadyExistsException();
        }

        leaveRequest.updateStatus(request.status());

        return leaveRequest;
    }

    @Transactional(readOnly = true)
    public Page<LeaveRequest> getAllLeaveRequest(final int page, final int size) {
        final Pageable pageable = PageRequest.of(page, size);

        return leaveRequestFindDao.findAll(pageable);
    }

    @Transactional
    public LeaveRequest updateLeaveRequestStatus(final Long leaveRequestId, final LeaveRequestStatusRequest requestDto) {
        final LeaveRequest leaveRequest = leaveRequestFindDao.findById(leaveRequestId);

        if (leaveRequest.getStatus() != Status.PENDING) {
            throw new StatusAlreadyExistsException();
        }

        leaveRequest.updateStatus(requestDto.status());

        return leaveRequest;
    }
}
