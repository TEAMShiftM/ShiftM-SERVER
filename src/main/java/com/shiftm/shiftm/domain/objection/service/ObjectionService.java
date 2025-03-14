package com.shiftm.shiftm.domain.objection.service;

import com.shiftm.shiftm.domain.objection.domain.Objection;
import com.shiftm.shiftm.domain.objection.dto.request.CreateObjectionReq;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ObjectionService {

    @Transactional
    public Objection createObjection(final String memberId, final CreateObjectionReq requestDto) {
    }
}
