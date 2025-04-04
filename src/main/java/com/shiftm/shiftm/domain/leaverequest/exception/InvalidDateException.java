package com.shiftm.shiftm.domain.leaverequest.exception;

import com.shiftm.shiftm.global.error.ErrorCode;
import com.shiftm.shiftm.global.error.exception.InvalidValueException;

import java.time.LocalDate;

public class InvalidDateException extends InvalidValueException {
    public InvalidDateException(final LocalDate date) {
        super(date.toString(), ErrorCode.INVALID_LEAVE_DATE);
    }
}