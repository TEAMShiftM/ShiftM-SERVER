package com.shiftm.shiftm.domain.leaverequest.dto;

import com.shiftm.shiftm.domain.leaverequest.dto.request.LeaveRequestPeriod;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ListLeaveRequestPeriodBuilder {
    public static List<LeaveRequestPeriod> build() {
        final List<LeaveRequestPeriod> leaveRequestPeriodList = new ArrayList<>();

        leaveRequestPeriodList.add(LeaveRequestPeriodBuilder.build(LocalDate.of(2025, 4, 1),
                LocalDate.of(2025, 4, 4), 8, null, null));

        leaveRequestPeriodList.add(LeaveRequestPeriodBuilder.build(LocalDate.of(2025, 4, 7),
                LocalDate.of(2025, 4, 11), 8, null, null));

        leaveRequestPeriodList.add(LeaveRequestPeriodBuilder.build(LocalDate.of(2025, 4, 14),
                LocalDate.of(2025, 4, 15), 8, null, null));

        return leaveRequestPeriodList;
    }
}