package com.shiftm.shiftm.domain.objection.repository;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.objection.domain.Objection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObjectionRepository extends JpaRepository<Objection, Long> {
    List<Objection> findAllByMember(final Member member);
}
