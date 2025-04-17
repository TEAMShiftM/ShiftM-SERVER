package com.shiftm.shiftm.domain.objection.repository;

import com.shiftm.shiftm.domain.objection.domain.Objection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjectionRepository extends JpaRepository<Objection, Long> {
}
