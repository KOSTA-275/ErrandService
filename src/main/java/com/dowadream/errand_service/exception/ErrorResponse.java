package com.dowadream.errand_service.exception;

/**
 * 에러 응답을 위한 클래스
 */
public class ErrorResponse {
    private String code;
    private String message;

    /**
     * ErrorResponse 생성자
     * @param code 에러 코드
     * @param message 에러 메시지
     */
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 에러 코드를 반환합니다.
     * @return 에러 코드
     */
    public String getCode() {
        return code;
    }

    /**
     * 에러 코드를 설정합니다.
     * @param code 설정할 에러 코드
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 에러 메시지를 반환합니다.
     * @return 에러 메시지
     */
    public String getMessage() {
        return message;
    }

    /**
     * 에러 메시지를 설정합니다.
     * @param message 설정할 에러 메시지
     */
    public void setMessage(String message) {
        this.message = message;
    }
}