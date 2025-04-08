package com.shiftm.shiftm.domain.leaverequest.service;

import com.shiftm.shiftm.domain.leave.domain.Leave;
import com.shiftm.shiftm.domain.leave.domain.LeaveBuilder;
import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import com.shiftm.shiftm.domain.leave.domain.LeaveTypeBuilder;
import com.shiftm.shiftm.domain.leave.repository.LeaveFindDao;
import com.shiftm.shiftm.domain.leave.repository.LeaveTypeFindDao;
import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequestBuilder;
import com.shiftm.shiftm.domain.leaverequest.domain.enums.Status;
import com.shiftm.shiftm.domain.leaverequest.dto.LeaveRequestStatusRequestBuilder;
import com.shiftm.shiftm.domain.leaverequest.dto.RequestLeaveRequestBuilder;
import com.shiftm.shiftm.domain.leaverequest.dto.request.LeaveRequestStatusRequest;
import com.shiftm.shiftm.domain.leaverequest.dto.request.RequestLeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.exception.InvalidDateException;
import com.shiftm.shiftm.domain.leaverequest.exception.LeaveNotEnoughException;
import com.shiftm.shiftm.domain.leaverequest.exception.StatusAlreadyExistsException;
import com.shiftm.shiftm.domain.leaverequest.repository.LeaveRequestFindDao;
import com.shiftm.shiftm.domain.leaverequest.repository.LeaveRequestRepository;
import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.domain.MemberBuilder;
import com.shiftm.shiftm.domain.member.repository.MemberFindDao;
import com.shiftm.shiftm.infra.holiday.HolidayClient;
import com.shiftm.shiftm.test.UnitTest;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LeaveRequestServiceTest extends UnitTest {

    @InjectMocks
    private LeaveRequestService leaveRequestService;

    @Mock
    private LeaveFindDao leaveFindDao;

    @Mock
    private LeaveTypeFindDao leaveTypeFindDao;

    @Mock
    private MemberFindDao memberFindDao;

    @Mock
    private LeaveRequestFindDao leaveRequestFindDao;

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private HolidayClient holidayClient;

    @BeforeEach
    void setUp() {
    }

    @Description("연차 요청 상태 변경 성공")
    @Test
    void 연차_요청_상태_변경_성공() {
        // given
        final Leave leave = LeaveBuilder.build();
        final LeaveRequestStatusRequest requestDto = LeaveRequestStatusRequestBuilder.build(Status.APPROVED);
        final LeaveRequest leaveRequest = LeaveRequestBuilder.build(Status.PENDING);

        leaveRequest.updateLeave(leave);

        when(leaveRequestFindDao.findById(any())).thenReturn(leaveRequest);

        // when
        final LeaveRequest updatedLeaveRequest = leaveRequestService.updateLeaveRequestStatus(1L, requestDto);

        // then
        assertThat(leaveRequest.getStatus()).isEqualTo(updatedLeaveRequest.getStatus());
        assertThat(leaveRequest.getLeave().getUsedCount()).isEqualTo(updatedLeaveRequest.getCount());
    }

    @Description("연차 요청 상태 변경 실패 - 이미 확정(승인 또는 거절)된 상태")
    @Test
    void 연차_요청_상태_변경_실패() {
        // given
        final LeaveRequestStatusRequest requestDto = LeaveRequestStatusRequestBuilder.build(Status.APPROVED);
        final LeaveRequest leaveRequest = LeaveRequestBuilder.build(Status.REJECTED);

        when(leaveRequestFindDao.findById(any())).thenReturn(leaveRequest);

        // when, then
        assertThrows(StatusAlreadyExistsException.class, () ->
                leaveRequestService.updateLeaveRequestStatus(1L, requestDto));
    }

    @Description("연차 요청 취소 성공")
    @Test
    void 연차_요청_취소_성공() {
        // given
        final Member member = Member.builder().build();
        final LeaveRequestStatusRequest requestDto = LeaveRequestStatusRequestBuilder.build(Status.CANCELED);
        final LeaveRequest leaveRequest = LeaveRequestBuilder.build(Status.PENDING);

        leaveRequest.updateMember(member);

        when(memberFindDao.findById(any())).thenReturn(member);
        when(leaveRequestFindDao.findById(any())).thenReturn(leaveRequest);

        // when
        final LeaveRequest updateLeaveRequest = leaveRequestService.cancelLeaveRequest("shiftM", 1L, requestDto);

        // then
        assertThat(updateLeaveRequest.getStatus()).isEqualTo(Status.CANCELED);
    }

    @Description("연차 요청 취소 실패 - 이미 확정(승인, 거절, 취소)된 상태")
    @Test
    void 연차_요청_취소_실패() {
        // given
        final Member member = Member.builder().build();
        final LeaveRequestStatusRequest requestDto = LeaveRequestStatusRequestBuilder.build(Status.PENDING);
        final LeaveRequest leaveRequest = LeaveRequestBuilder.build(Status.CANCELED);

        leaveRequest.updateMember(member);

        when(memberFindDao.findById(any())).thenReturn(member);
        when(leaveRequestFindDao.findById(any())).thenReturn(leaveRequest);

        // when, then
        assertThrows(StatusAlreadyExistsException.class, () ->
                leaveRequestService.cancelLeaveRequest("shiftM", 1L, requestDto));
    }

    @Description("연차 신청 성공")
    @Test
    void 연차_신청_성공() {
        // given
        final Member member = MemberBuilder.build();
        final Leave leave = LeaveBuilder.build();
        final LeaveType leaveType = LeaveTypeBuilder.build();
        final LeaveRequest leaveRequest = LeaveRequestBuilder.build(Status.PENDING);

        leaveRequest.updateMember(member);
        leaveRequest.updateLeave(leave);

        leave.updateLeaveType(leaveType);
        leave.updateMember(member);

        final RequestLeaveRequest requestDto = RequestLeaveRequestBuilder.build(LocalDate.of(2025, 5, 2),
                LocalDate.of(2025, 5, 7));

        final List<LocalDate> holidayList = List.of(LocalDate.of(2025, 5, 3),
                LocalDate.of(2025, 5, 4),
                LocalDate.of(2025, 5, 5),
                LocalDate.of(2025, 5, 6));

        when(memberFindDao.findById(any())).thenReturn(member);
        when(leaveFindDao.findByMemberIdAndLeaveTypeAndExpirationDateGreaterThanEqual(any(), any(), any())).thenReturn(leave);
        when(holidayClient.getHolidaysBetweenDates(any(), any())).thenReturn(holidayList);

        when(leaveRequestRepository.save(any())).thenReturn(leaveRequest);

        // when
        final LeaveRequest savedLeaveRequest = leaveRequestService.requestLeave(null, requestDto);

        // then
        assertThat(savedLeaveRequest.getCount()).isEqualTo(2.0);
    }

    @Description("연차 신청 실패 - 연차 신청 시작일이 주말인 경우")
    @Test
    void 연차_신청_실패_연차_신청_시작일_주말() {
        // given
        final Member member = MemberBuilder.build();
        final Leave leave = LeaveBuilder.build();
        final LeaveType leaveType = LeaveTypeBuilder.build();

        leave.updateLeaveType(leaveType);
        leave.updateMember(member);

        final RequestLeaveRequest requestDto = RequestLeaveRequestBuilder.build(LocalDate.of(2025, 5, 3),
                LocalDate.of(2025, 5, 3));

        when(leaveFindDao.findByMemberIdAndLeaveTypeAndExpirationDateGreaterThanEqual(any(), any(), any())).thenReturn(leave);
        when(holidayClient.getHolidaysBetweenDates(any(), any())).thenReturn(List.of());

        // when, then
        assertThrows(InvalidDateException.class, () -> leaveRequestService.requestLeave(null, requestDto));
    }

    @Description("연차 신청 실패 - 연차 신청 시작일이 공휴일인 경우")
    @Test
    void 연차_신청_실패_연차_신청_시작일_공휴일() {
        // given
        final Member member = MemberBuilder.build();
        final Leave leave = LeaveBuilder.build();
        final LeaveType leaveType = LeaveTypeBuilder.build();

        leave.updateLeaveType(leaveType);
        leave.updateMember(member);

        final RequestLeaveRequest requestDto = RequestLeaveRequestBuilder.build(LocalDate.of(2025, 5, 5),
                LocalDate.of(2025, 5, 5));

        when(leaveFindDao.findByMemberIdAndLeaveTypeAndExpirationDateGreaterThanEqual(any(), any(), any())).thenReturn(leave);
        when(holidayClient.getHolidaysBetweenDates(any(), any())).thenReturn(List.of(LocalDate.of(2025, 5, 5)));

        // when, then
        assertThrows(InvalidDateException.class, () -> leaveRequestService.requestLeave(null, requestDto));
    }

    @Description("연차 신청 실패 - 연차 부족")
    @Test
    void 연차_신청_실패_연차_부족() {
        // given
        final Member member = MemberBuilder.build();
        final Leave leave = LeaveBuilder.build();
        final LeaveType leaveType = LeaveTypeBuilder.build();
        final LeaveRequest leaveRequest = LeaveRequestBuilder.build(Status.PENDING);

        leaveRequest.updateMember(member);
        leaveRequest.updateLeave(leave);

        leave.updateLeaveType(leaveType);
        leave.updateMember(member);

        final RequestLeaveRequest requestDto = RequestLeaveRequestBuilder.build(LocalDate.of(2025, 5, 2),
                LocalDate.of(2025, 5, 20));

        when(leaveFindDao.findByMemberIdAndLeaveTypeAndExpirationDateGreaterThanEqual(any(), any(), any())).thenReturn(leave);
        when(holidayClient.getHolidaysBetweenDates(any(), any())).thenReturn(List.of());

        // when, then
        assertThrows(LeaveNotEnoughException.class, () -> leaveRequestService.requestLeave(null, requestDto));
    }
}
