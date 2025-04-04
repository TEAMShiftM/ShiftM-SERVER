package com.shiftm.shiftm.domain.leave.api;

import com.querydsl.core.Tuple;
import com.shiftm.shiftm.domain.leave.domain.Leave;
import com.shiftm.shiftm.domain.leave.dto.response.LeaveCountResponse;
import com.shiftm.shiftm.domain.leave.dto.response.ListLeaveResponse;
import com.shiftm.shiftm.domain.leave.service.LeaveService;
import com.shiftm.shiftm.global.auth.annotation.AuthId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/leave")
@RestController
public class LeaveController {

    private final LeaveService leaveService;

    @GetMapping("/{leaveTypeId}")
    public LeaveCountResponse getLeaveCountByLeaveType(@AuthId final String memberId,
                                                       @PathVariable("leaveTypeId") final Long leaveTypeId) {
        final Tuple leaveCount = leaveService.getLeaveCountByLeaveType(memberId, leaveTypeId);

        return LeaveCountResponse.of(leaveCount);
    }

    @GetMapping
    public ListLeaveResponse getLeaveByMember(@AuthId final String memberId,
                                              @RequestParam(defaultValue = "0") final int page,
                                              @RequestParam(defaultValue = "10") final int size) {
        final Page<Leave> leaveList = leaveService.getLeaveByMember(memberId, page, size);

        return ListLeaveResponse.of(leaveList.getContent(), leaveList.getNumber(), leaveList.getSize(),
                leaveList.getTotalPages(), leaveList.getTotalElements());
    }
}
