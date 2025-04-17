package com.shiftm.shiftm.domain.objection.service;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.repository.MemberFindDao;
import com.shiftm.shiftm.domain.objection.domain.Objection;
import com.shiftm.shiftm.domain.objection.dto.request.CreateObjectionReq;
import com.shiftm.shiftm.domain.objection.repository.ObjectionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ObjectionService {

    private final ObjectionRepository objectionRepository;
    private final MemberFindDao memberFindDao;

    // TODO : ShiftId 유효성 검사 추가
    @Transactional
    public Objection createObjection(final String memberId, final CreateObjectionReq requestDto) {
        final Member member = memberFindDao.findById(memberId);
        return objectionRepository.save(requestDto.toEntity(member));
    }
}
