package queuing.core.global.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("false")
public final class FailedResponseBody extends ResponseBody<Void> {
    private final ErrorBody error;

    public FailedResponseBody(){
        this(null, null);
    }

    public FailedResponseBody(String code, String message){
        super(false);
        this.error = new ErrorBody(code, message);
    }

    private record ErrorBody(
        String code,
        String message
    ) {
    }

    public ErrorBody getError() {
        return error;
    }
}
