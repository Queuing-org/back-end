package queuing.core.global.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "success"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SuccessResponseBody.class, name = "true"),
    @JsonSubTypes.Type(value = FailedResponseBody.class, name = "false")
})
public sealed abstract class ResponseBody<T> permits SuccessResponseBody, FailedResponseBody {
    private final boolean success;

    public ResponseBody(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public static ResponseBody<Void> success() {
        return new SuccessResponseBody<>();
    }

    public static <T> ResponseBody<T> success(T data) {
        return new SuccessResponseBody<>(data);
    }

    public static ResponseBody<Void> error() {
        return new FailedResponseBody();
    }

    public static ResponseBody<Void> error(String code, String message) {
        return new FailedResponseBody(code, message);
    }
}
