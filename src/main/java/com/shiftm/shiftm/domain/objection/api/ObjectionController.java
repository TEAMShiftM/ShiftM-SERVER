package com.shiftm.shiftm.domain.objection.api;

import com.shiftm.shiftm.domain.objection.domain.Objection;
import com.shiftm.shiftm.domain.objection.dto.request.CreateObjectionReq;
import com.shiftm.shiftm.domain.objection.dto.response.ListObjectionRes;
import com.shiftm.shiftm.domain.objection.dto.response.ObjectionRes;
import com.shiftm.shiftm.domain.objection.service.ObjectionService;
import com.shiftm.shiftm.global.auth.annotation.AuthId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/objection")
@RestController
public class ObjectionController {

    private final ObjectionService objectionService;

    @PostMapping
    public ObjectionRes createObjection(@AuthId final String memberId, @Valid @RequestBody final CreateObjectionReq requestDto) {
        final Objection objection =  objectionService.createObjection(memberId, requestDto);
        return new ObjectionRes(objection);
    }

    @GetMapping
    public ListObjectionRes getObjectionList(@AuthId final String memberId) {
        final List<Objection> objectionList = objectionService.getObjectionList(memberId);
        return ListObjectionRes.of(objectionList);
    }
}
