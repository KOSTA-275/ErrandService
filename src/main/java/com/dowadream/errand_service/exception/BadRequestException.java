package com.dowadream.errand_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 잘못된 요청을 나타내는 예외 클래스
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 메시지와 함께 BadRequestException을 생성합니다.
     * @param message 예외 메시지
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * 메시지와 원인 예외와 함께 BadRequestException을 생성합니다.
     * @param message 예외 메시지
     * @param cause 원인 예외
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}