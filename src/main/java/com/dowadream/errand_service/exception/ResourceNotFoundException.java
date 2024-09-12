package com.dowadream.errand_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 리소스를 찾을 수 없을 때 발생하는 예외 클래스
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 메시지와 함께 ResourceNotFoundException을 생성합니다.
     * @param message 예외 메시지
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * 메시지와 원인 예외와 함께 ResourceNotFoundException을 생성합니다.
     * @param message 예외 메시지
     * @param cause 원인 예외
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}