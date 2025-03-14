package com.shiftm.shiftm.domain.objection.api;

import com.shiftm.shiftm.domain.objection.dto.request.CreateObjectionReq;
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
    public void createObjection(@Valid @RequestBody final CreateObjectionReq requestDto) {
        objectionService.createObjection(requestDto);
    }
}
