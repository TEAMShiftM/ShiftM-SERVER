package com.shiftm.shiftm.domain.leaverequest.api;

import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.dto.request.RequestLeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.dto.request.LeaveRequestStatusRequest;
import com.shiftm.shiftm.domain.leaverequest.dto.response.ListLeaveRequestResponse;
import com.shiftm.shiftm.domain.leaverequest.dto.response.LeaveRequestResponse;
import com.shiftm.shiftm.domain.leaverequest.service.LeaveRequestService;
import com.shiftm.shiftm.global.auth.annotation.AuthId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/leave-request")
@RestController
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @PostMapping
    public void requestLeave(@AuthId final String memberId, @Valid @RequestBody final RequestLeaveRequest requestDto) {
        leaveRequestService.requestLeave(memberId, requestDto);
    }

    @GetMapping
    public ListLeaveRequestResponse getRequestLeaveByMember(@AuthId final String memberId,
                                                            @RequestParam(defaultValue = "0") final int page,
                                                            @RequestParam(defaultValue = "10") final int size) {
        final Page<LeaveRequest> leaveRequestList = leaveRequestService.getLeaveRequestByMember(memberId, page, size);

        return ListLeaveRequestResponse.of(leaveRequestList.getContent(), leaveRequestList.getNumber(),
                leaveRequestList.getSize(), leaveRequestList.getTotalPages(), leaveRequestList.getTotalElements());
    }

    @PatchMapping("/{leaveRequestId}")
    public LeaveRequestResponse cancelLeaveRequest(@AuthId final String memberId,
                                                   @PathVariable("leaveRequestId") final Long leaveRequestId,
                                                   @Valid @RequestBody final LeaveRequestStatusRequest requestDto) {
        final LeaveRequest leaveRequest = leaveRequestService.cancelLeaveRequest(memberId, leaveRequestId, requestDto);

        return new LeaveRequestResponse(leaveRequest);
    }
}
