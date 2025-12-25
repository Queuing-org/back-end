package queuing.core.global.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import com.fasterxml.jackson.annotation.JsonInclude;

public final class FailedResponseBody implements ResponseBody<Void> {
    private final ErrorContent error;

    public FailedResponseBody(){
        this(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, null);
    }

    public FailedResponseBody(int statusCode, String code, String message) {
        this.error = new ErrorContent(statusCode, code, message, null);
    }

    public FailedResponseBody(int statusCode, String code, String message, BindingResult bindingResult) {
        this.error = new ErrorContent(statusCode, code, message, FieldError.from(bindingResult));
    }

    public ErrorContent getError() {
        return error;
    }

    public record ErrorContent(
        int statusCode,
        String code,
        String message,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<FieldError> fieldErrors
    ) {}

    public record FieldError(
        String field,
        String value,
        String reason
    ) {
        public static List<FieldError> from(BindingResult bindingResult) {
            return bindingResult.getFieldErrors().stream()
                .map(error -> new FieldError(
                    error.getField(),
                    error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                    error.getDefaultMessage()
                ))
                .toList();
        }
    }
}
