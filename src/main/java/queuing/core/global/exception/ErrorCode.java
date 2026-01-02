package queuing.core.global.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // Common
    COMMON_INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, null, null),
    COMMON_METHOD_NOT_ALLOWED(METHOD_NOT_ALLOWED, null, null),
    COMMON_INVALID_INPUT(BAD_REQUEST, "invalid-input", "입력 값이 잘못되었어요."),

    // User
    USER_NOT_FOUND(NOT_FOUND, "user.not-found", "사용자 정보가 존재하지 않아요."),
    USER_INVALID_INPUT(BAD_REQUEST, "user.invalid-input", "아이디 또는 비밀번호가 일치하지 않아요."),
    USER_NOT_AUTHENTICATED(UNAUTHORIZED, "user.not-authenticated", "인증된 사용자만 요청할 수 있어요."),
    USER_INSUFFICIENT_SCOPE(FORBIDDEN, "user.insufficient-scope", "특정 권한을 가진 사용자만 요청할 수 있어요."),
    USER_ONBOARDING_REQUIRED(FORBIDDEN, "user.nickname-required", "닉네임 설정이 필요합니다."),
    USER_NICKNAME_DUPLICATED(CONFLICT, "user.nickname-duplicated", "이미 사용 중인 닉네임이에요."),
    USER_NICKNAME_FORBIDDEN(BAD_REQUEST, "user.nickname-forbidden", "사용할 수 없는 닉네임이에요."),

    // Friend
    FRIEND_ALREADY_EXISTS(CONFLICT, "friend.already-exists", "이미 친구 관계거나 요청 중이에요."),
    FRIEND_REQUEST_NOT_FOUND(NOT_FOUND, "friend.request-not-found", "친구 요청을 찾을 수 없어요."),
    FRIEND_INVALID_REQUEST(BAD_REQUEST, "friend.invalid-request", "허용되지 않은 친구 요청이에요."),
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
