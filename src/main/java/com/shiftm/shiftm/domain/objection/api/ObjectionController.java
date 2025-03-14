package com.shiftm.shiftm.domain.objection.api;

import com.shiftm.shiftm.domain.objection.domain.Objection;
import com.shiftm.shiftm.domain.objection.dto.request.CreateObjectionReq;
import com.shiftm.shiftm.domain.objection.dto.response.ObjectionRes;
import com.shiftm.shiftm.domain.objection.service.ObjectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/objection")
@RestController
public class ObjectionController {

    private final ObjectionService objectionService;

    @PostMapping
    public ObjectionRes createObjection(@Valid @RequestBody final CreateObjectionReq requestDto) {
        final Objection objection =  objectionService.createObjection(requestDto);
        return new ObjectionRes(objection);
    }
}
