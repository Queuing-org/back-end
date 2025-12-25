package queuing.core.global.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    // Common
    COMMON_INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, null, null),
    COMMON_METHOD_NOT_ALLOWED(METHOD_NOT_ALLOWED, null, null),
    COMMON_INVALID_INPUT(BAD_REQUEST, "invalid-input", "입력 값이 잘못되었어요."),

    // User
    USER_NOT_FOUND(NOT_FOUND, "user.not-found", "사용자 정보가 존재하지 않아요."),
    USER_INVALID_INPUT(BAD_REQUEST, "user.invalid-input", "아이디 또는 비밀번호가 일치하지 않아요."),
    USER_NOT_AUTHENTICATED(BAD_REQUEST, "user.not-authenticated", "인증된 사용자만 요청할 수 있어요."),
    USER_NICKNAME_REQUIRED(FORBIDDEN, "user.nickname-required", "닉네임 설정이 필요합니다."),
    USER_NICKNAME_DUPLICATED(CONFLICT, "user.nickname-duplicated", "이미 사용 중인 닉네임이에요."),
    USER_NICKNAME_FORBIDDEN(BAD_REQUEST, "user.nickname-forbidden", "사용할 수 없는 닉네임이에요."),

    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public int getStatusCode() {
        return status.value();
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return this.message;
    }
}
