package queuing.core.global.response;

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
}
