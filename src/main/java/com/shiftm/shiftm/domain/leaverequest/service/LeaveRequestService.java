package com.shiftm.shiftm.domain.leaverequest.service;

import com.shiftm.shiftm.domain.leave.domain.Leave;
import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import com.shiftm.shiftm.domain.leave.repository.LeaveFindDao;
import com.shiftm.shiftm.domain.leave.repository.LeaveTypeFindDao;
import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.domain.enums.Status;
import com.shiftm.shiftm.domain.leaverequest.dto.request.LeaveRequestPeriod;
import com.shiftm.shiftm.domain.leaverequest.dto.request.RequestLeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.dto.request.LeaveRequestStatusRequest;
import com.shiftm.shiftm.domain.leaverequest.exception.*;
import com.shiftm.shiftm.domain.leaverequest.repository.LeaveRequestFindDao;
import com.shiftm.shiftm.domain.leaverequest.repository.LeaveRequestRepository;
import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.repository.MemberFindDao;
import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.infra.holiday.HolidayClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LeaveRequestService {

    private final LeaveRequestFindDao leaveRequestFindDao;
    private final MemberFindDao memberFindDao;
    private final LeaveFindDao leaveFindDao;
    private final LeaveTypeFindDao leaveTypeFindDao;
    private final LeaveRequestRepository leaveRequestRepository;
    private final HolidayClient holidayClient;

    // TODO 주석 제거하기
    @Transactional
    public List<LeaveRequest> requestLeave(final String memberId, final RequestLeaveRequest requestDto) {
        final LeaveType leaveType = leaveTypeFindDao.findValidById(requestDto.leaveTypeId());

        final Member member = memberFindDao.findById(memberId);

        // 1. 연차 Entity 가져오기 (연차가 없다면 LeaveNotEnoughException 에러 발생)
        final Leave leave = leaveFindDao.findByMemberIdAndLeaveTypeAndExpirationDateGreaterThanEqual(memberId, leaveType, LocalDate.now());

        // 2. 사용 가능한 연차 수 계산
        final Double usableCount = validateUsableLeave(leave);

        // 3. 기간 내 공휴일 가져오기
        final Set<LocalDate> holidayList = getHolidayListInRange(requestDto.periodList());

        // 4. 시작일 및 종료일이 공휴일 또는 주말 여부 검증
        validatePeriod(requestDto.periodList(), holidayList);

        // 5. 연차 신청 엔티티 생성
        final List<LeaveRequest> leaveRequestList = createLeaveRequestList(requestDto, usableCount, member, leave, holidayList);

        return leaveRequestRepository.saveAll(leaveRequestList);
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

    private Set<LocalDate> getHolidayListInRange(final List<LeaveRequestPeriod> periodList) {
        final LocalDate startDate = periodList.stream()
                .map(LeaveRequestPeriod::startDate)
                .min(LocalDate::compareTo)
                .get();

        final LocalDate endDate = periodList.stream()
                .map(LeaveRequestPeriod::endDate)
                .max(LocalDate::compareTo)
                .get();

        return holidayClient.getHolidaysBetweenDates(startDate, endDate).stream()
                .collect(Collectors.toSet());
    }

    private void validatePeriod(List<LeaveRequestPeriod> periodList, Set<LocalDate> holidayList) {
        for (LeaveRequestPeriod period : periodList) {
            if (holidayList.contains(period.startDate()) || period.startDate().getDayOfWeek() == DayOfWeek.SATURDAY ||
                    period.startDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                throw new InvalidDateException(period.startDate());
            }

            if (holidayList.contains(period.endDate()) || period.endDate().getDayOfWeek() == DayOfWeek.SATURDAY ||
                    period.endDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                throw new InvalidDateException(period.endDate());
            }
        }
    }

    private List<LocalDate> getValidDateList(final LeaveRequestPeriod period, final Set<LocalDate> holidayList) {
        final List<LocalDate> dateList = new ArrayList<>();

        LocalDate currentDate = period.startDate();

        while (!currentDate.isAfter(period.endDate())) {
            dateList.add(currentDate);

            currentDate = currentDate.plusDays(1);

            final LocalDate lastDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

            if (currentDate.equals(lastDate)) {
                currentDate = currentDate.plusMonths(1).withDayOfMonth(1);
                dateList.add(currentDate);
            }
        }

        return dateList.stream()
                .filter(date -> !holidayList.contains(date) && date.getDayOfWeek() != DayOfWeek.SATURDAY &&
                        date.getDayOfWeek() != DayOfWeek.SUNDAY)
                .collect(Collectors.toList());
    }

    private Double validateUsableLeave(Leave leave) {
        final Double usableCount = ((leave.getCount() * 100) - (leave.getUsedCount() * 100)) / 100;

        if (usableCount < 0) {
            throw new LeaveNotEnoughException(ErrorCode.LEAVE_NOT_ENOUGH);
        }

        return usableCount;
    }

    private Double updateTotalRequestCount(Double totalRequestCount, final Double increment, final Double usableCount) {
        totalRequestCount = ((int) (totalRequestCount * 100) + (int) (increment * 100)) / 100.0;

        if (totalRequestCount > usableCount) {
            throw new LeaveNotEnoughException(ErrorCode.LEAVE_NOT_ENOUGH);
        }

        return totalRequestCount;
    }

    private List<LeaveRequest> createLeaveRequestList(final RequestLeaveRequest requestDto, final Double usableCount,
                                                      final Member member, final Leave leave,
                                                      final Set<LocalDate> holidayList) {
        final List<LeaveRequest> leaveRequestList = new ArrayList<>();

        Double totalRequestCount = 0.0;

        for (LeaveRequestPeriod period : requestDto.periodList()) {
            // 반차 또는 반반차라면
            if (period.hour() < 8) {
                // 요청한 연차 수 검증 (만약 신청한 연차의 수가 잔여 수 보다 많다면 에러 발생)
                totalRequestCount = updateTotalRequestCount(totalRequestCount, period.hour() / 8.0, usableCount);

                final LeaveRequest leaveRequest = LeaveRequest.builder()
                        .startDate(period.startDate())
                        .endDate(period.endDate())
                        .startTime(period.startTime())
                        .endTime(period.endTime())
                        .count(period.hour() / 8.0)
                        .status(Status.PENDING).build();

                leaveRequest.updateMember(member);
                leaveRequest.updateLeave(leave);

                leaveRequestList.add(leaveRequest);
            } else {
                // 시작일부터 종료일까지 휴일 또는 공휴일 제외한 날 가져오기
                List<LocalDate> validDateList = getValidDateList(period, holidayList);

                totalRequestCount = updateTotalRequestCount(totalRequestCount, (double) validDateList.size(), usableCount);

                final LeaveRequest leaveRequest = LeaveRequest.builder()
                        .startDate(period.startDate())
                        .endDate(period.endDate())
                        .count((double) validDateList.size())
                        .status(Status.PENDING).build();

                leaveRequest.updateMember(member);
                leaveRequest.updateLeave(leave);

                leaveRequestList.add(leaveRequest);
            }
        }

        return leaveRequestList;
    }
}
