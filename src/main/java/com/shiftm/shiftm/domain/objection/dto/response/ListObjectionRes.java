package com.shiftm.shiftm.domain.objection.dto.response;

import com.shiftm.shiftm.domain.objection.domain.Objection;

import java.util.List;
import java.util.stream.Collectors;

public record ListObjectionRes(
        List<ObjectionRes> objectionList
) {
    public static ListObjectionRes of(final List<Objection> objectionList) {
        final List<ObjectionRes> objectionResList = objectionList.stream()
                .map(ObjectionRes::new)
                .collect(Collectors.toList());

        return new ListObjectionRes(objectionResList);
    }
}
