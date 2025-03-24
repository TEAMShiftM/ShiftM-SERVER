package com.shiftm.shiftm.domain.leaverequest.repository;

import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.exception.LeaveRequestNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LeaveRequestFindDao {

    private final LeaveRequestRepository leaveRequestRepository;
    private final CustomLeaveRequestRepository customLeaveRequestRepository;

    public LeaveRequest findById(final Long id) {
        return leaveRequestRepository.findById(id).orElseThrow(LeaveRequestNotFoundException::new);
    }

    public Page<LeaveRequest> findAll(final Pageable pageable) {
        return customLeaveRequestRepository.findAll(pageable);
    }

    public Page<LeaveRequest> findByMember(final String memberId, final Pageable pageable) {
        return customLeaveRequestRepository.findByMember(memberId, pageable);
    }
}