package com.echoverse.profile.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    INVALID_INPUT_VALUE(400, "Invalid input value", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS(401, "Invalid credentials", HttpStatus.UNAUTHORIZED),
    UNAUTHENTICATED(401, "Unauthenticated access", HttpStatus.UNAUTHORIZED),
    NOT_NULL_CONSTRAINT(402, "Not null constraint violation", HttpStatus.PAYMENT_REQUIRED),
    UNAUTHORIZED(403, "Unauthorized access", HttpStatus.FORBIDDEN),
    USER_NOT_FOUND(404, "User not found", HttpStatus.NOT_FOUND),
    PERMISSION_NOT_FOUND(404, "Permission not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(404, "Role not found", HttpStatus.NOT_FOUND),
    PROFILE_NOT_FOUND(404, "Profile not found", HttpStatus.NOT_FOUND),
    FAIL_TO_GENERATE_TOKEN(406, "Fail to generate token", HttpStatus.NOT_ACCEPTABLE),
    INVALID_JWT_TOKEN(407, "Invalid JWT token", HttpStatus.PROXY_AUTHENTICATION_REQUIRED),
    USER_EXISTED(409, "User already exists", HttpStatus.CONFLICT),
    PHONE_NUMBER_EXISTED(409, "Phone number already exists", HttpStatus.CONFLICT),
    EMAIL_EXISTED(409, "Email already exists", HttpStatus.CONFLICT),
    PERMISSION_ALREADY_EXISTS(409, "Permission already exists", HttpStatus.CONFLICT),
    ROLE_ALREADY_EXISTS(409, "Role already exists", HttpStatus.CONFLICT),
    INVALID_DATE_OF_BIRTH(422, "User must be at least {min} years old", HttpStatus.UNPROCESSABLE_ENTITY),
    UNCATEGORIZED(500, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR);

    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
