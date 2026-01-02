package queuing.core.global.response;

import org.springframework.validation.BindingResult;

public sealed interface ResponseBody<T> permits SuccessResponseBody, FailedResponseBody {
    static ResponseBody<Void> success() {
        return new SuccessResponseBody<>();
    }

    static <T> ResponseBody<T> success(T data) {
        return new SuccessResponseBody<>(data);
    }

    static ResponseBody<Void> error() {
        return new FailedResponseBody();
    }

    static ResponseBody<Void> error(int statusCode, String code, String message) {
        return new FailedResponseBody(statusCode, code, message);
    }

    static ResponseBody<Void> error(int statusCode, String code, String message, BindingResult bindingResult) {
        return new FailedResponseBody(statusCode, code, message, bindingResult);
    }
}
