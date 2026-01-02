package queuing.core.global.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import queuing.core.global.response.FailedResponseBody;
import queuing.core.global.response.ResponseBody;

@RestControllerAdvice
public class ExceptionAdvice {
    private static final Logger log = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ResponseBody<Void>> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(new FailedResponseBody(
                errorCode.getStatusCode(),
                errorCode.getCode(),
                errorCode.getMessage()
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleRuntimeException(final RuntimeException e) {
        log.warn("{}", e.getMessage(), e);

        ErrorCode errorCode = ErrorCode.COMMON_INTERNAL_SERVER_ERROR;

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ResponseBody.error());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ResponseBody<Void>> handleNoResourceFoundException(NoResourceFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseBody<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ErrorCode errorCode = ErrorCode.COMMON_METHOD_NOT_ALLOWED;

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ResponseBody.error(
                errorCode.getStatusCode(),
                errorCode.getCode(),
                errorCode.getMessage()
            ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ResponseBody<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.COMMON_INVALID_INPUT;

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ResponseBody.error(
                errorCode.getStatusCode(),
                errorCode.getCode(),
                errorCode.getMessage(),
                e.getBindingResult()
            ));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    protected ResponseEntity<ResponseBody<Void>> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        ErrorCode errorCode = ErrorCode.USER_NOT_AUTHENTICATED;

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ResponseBody.error(
                errorCode.getStatusCode(),
                errorCode.getCode(),
                errorCode.getMessage()
            ));
    }
}
