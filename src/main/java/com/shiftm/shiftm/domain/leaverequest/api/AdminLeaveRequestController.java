package com.shiftm.shiftm.domain.leaverequest.api;

import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.dto.request.LeaveRequestStatusRequest;
import com.shiftm.shiftm.domain.leaverequest.dto.response.AdminLeaveRequestResponse;
import com.shiftm.shiftm.domain.leaverequest.dto.response.ListAdminLeaveRequestResponse;
import com.shiftm.shiftm.domain.leaverequest.service.LeaveRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/admin/leave-request")
@RestController
public class AdminLeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @GetMapping
    public ListAdminLeaveRequestResponse getAllLeaveRequest(@RequestParam(defaultValue = "0") final int page,
                                                            @RequestParam(defaultValue = "10") final int size) {
        final Page<LeaveRequest> leaveRequestList = leaveRequestService.getAllLeaveRequest(page, size);

        return ListAdminLeaveRequestResponse.of(leaveRequestList.getContent(), leaveRequestList.getNumber(),
                leaveRequestList.getSize(), leaveRequestList.getTotalPages(), leaveRequestList.getTotalElements());
    }

    @GetMapping("/{memberId}")
    public ListAdminLeaveRequestResponse getLeaveRequest(@PathVariable("memberId") final String memberId,
                                                         @RequestParam(defaultValue = "0") final int page,
                                                         @RequestParam(defaultValue = "10") final int size) {
        final Page<LeaveRequest> leaveRequestList = leaveRequestService.getLeaveRequestByMember(memberId, page, size);

        return ListAdminLeaveRequestResponse.of(leaveRequestList.getContent(), leaveRequestList.getNumber(),
                leaveRequestList.getSize(), leaveRequestList.getTotalPages(), leaveRequestList.getTotalElements());
    }

    @PatchMapping("/{leaveRequestId}")
    public AdminLeaveRequestResponse updateLeaveRequest(@PathVariable("leaveRequestId") final Long leaveRequestId,
                                                        @Valid @RequestBody final LeaveRequestStatusRequest requestDto) {
        final LeaveRequest leaveRequest = leaveRequestService.updateLeaveRequestStatus(leaveRequestId, requestDto);

        return new AdminLeaveRequestResponse(leaveRequest);
    }
}