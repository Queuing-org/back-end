package queuing.core.global.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;

@Getter
@JsonTypeName("true")
public final class SuccessResponseBody<T> extends ResponseBody<T> {
    private final T result;

    public SuccessResponseBody() {
        this(null);
    }

    public SuccessResponseBody(T result) {
        super(true);
        this.result = result;
    }

    public T getResult() {
        return result;
    }
}
