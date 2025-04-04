package com.shiftm.shiftm.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    /* COMMON ERROR */
    INTERNAL_SERVER_ERROR(500, "COMMON001", "Internal Server Error"),
    INVALID_INPUT_VALUE(400, "COMMON002", "Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "COMMON003", "Entity Not Found"),

    /* MEMBER ERROR */
    DUPLICATED_ID(400, "MEM001", "It Is Duplicated ID"),
    DUPLICATED_EMAIL(400, "MEM002", "It Is Duplicated Email"),
    MEMBER_NOT_FOUND(400, "MEM003", "Member Not Found"),
    PASSWORD_NOT_MATCH(400, "MEM004", "Password Not Match"),

    /* AUTH ERROR */
    INVALID_PASSWORD(400, "AUTH001", "It Is Invalid Password"),
    INVALID_TOKEN(400, "AUTH002", "It Is Invalid Token"),
    INVALID_BEARER_PREFIX(400, "AUTH003", "It Is Invalid Bearer Prefix"),
    UNAUTHORIZED(401, "AUTH004", "Unauthorized, Please Login"),
    FORBIDDEN(403, "AUTH005", "Forbidden, You Don't Have Any Authority"),

    /* LEAVE TYPE ERROR */
    DUPLICATED_NAME(400, "LVT001", "It Is Duplicated Name"),
    LEAVE_TYPE_NOT_FOUND(404, "LVT002", "Leave type not found"),
    LEAVE_TYPE_LOCKED(400, "LVT003", "Statutory leave type cannot modify or delete"),

    /* LEAVE ERROR */
    LEAVE_EXISTS(400, "LEV001", "Leave exists and cannot be deleted"),

    /* COMPANY ERROR */
    COMPANY_ALREADY_EXISTS(404, "COMPANY001", "Company Is Already Exists"),
    COMPANY_NOT_FOUND(404, "COMPANY002", "Company Not Found"),

    /* LEAVE REQUEST ERROR */
    LEAVE_NOT_ENOUGH(400, "LVR001", "Leave Not Enough"),
    LEAVE_REQUEST_NOT_AUTHOR(401, "LVR002", "Not the author."),
    INVALID_LEAVE_DATE(400, "LVR003", "Leave start or end date cannot be a holiday or weekend"),
    LEAVE_REQUEST_NOT_FOUND(404, "LVR004", "Leave request Not Found"),
    STATUS_ALREADY_EXISTS(400, "LVR005", "Leave request status already exists"),

    /* SHIFT ERROR */
    CHECKIN_ALREADY_EXISTS(404, "SHIFT001", "Checkin Is Already Exists"),
    SHIFT_NOT_FOUND(404, "SHIFT002", "Shift Not Found"),
  
    /* EMAIL ERROR */
    UNABLE_TO_SEND_EMAIL(500, "EMAIL001", "Unable To Send Email"),

    /* GEOCODING ERROR */
    GEOCODING_FAILED(500, "GEO001", "Failed To Convert Coordinates To An Address"),
    ADDRESS_NOT_FOUND(404, "GEO002", "No Address Found For The Given Coordinates");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
