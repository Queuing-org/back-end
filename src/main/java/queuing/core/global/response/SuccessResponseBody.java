package queuing.core.global.response;

public final class SuccessResponseBody<T> implements ResponseBody<T> {
    private final T result;

    public SuccessResponseBody() {
        this(null);
    }

    public SuccessResponseBody(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }
}
