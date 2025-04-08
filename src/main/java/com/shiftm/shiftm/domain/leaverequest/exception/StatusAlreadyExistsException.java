package com.shiftm.shiftm.domain.leaverequest.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.BusinessException;

public class StatusAlreadyExistsException extends BusinessException {
    public StatusAlreadyExistsException() {
        super(ErrorCode.STATUS_ALREADY_EXISTS);
    }
}