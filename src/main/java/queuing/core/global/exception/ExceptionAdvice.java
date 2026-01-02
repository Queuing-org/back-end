package queuing.core.global.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import queuing.core.global.response.FailedResponseBody;
import queuing.core.global.response.ResponseBody;

@RestControllerAdvice
public class ExceptionAdvice {
    private static final Logger log = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ResponseBody<Void>> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(new FailedResponseBody(
                errorCode.getStatusCode(),
                errorCode.getCode(),
                errorCode.getMessage()
            ));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleException(final Exception ex) {
        log.warn("{}", ex.getMessage(), ex);

        ErrorCode errorCode = ErrorCode.COMMON_INTERNAL_SERVER_ERROR;

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ResponseBody.error());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ResponseBody<Void>> handleNoResourceFoundException(NoResourceFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ResponseBody<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ErrorCode errorCode = ErrorCode.COMMON_METHOD_NOT_ALLOWED;

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ResponseBody.error(
                errorCode.getStatusCode(),
                errorCode.getCode(),
                errorCode.getMessage()
            ));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    protected ResponseEntity<ResponseBody<Void>> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        ErrorCode errorCode = ErrorCode.USER_NOT_AUTHENTICATED;

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ResponseBody.error(
                errorCode.getStatusCode(),
                errorCode.getCode(),
                errorCode.getMessage()
            ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ResponseBody<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorCode errorCode = ErrorCode.COMMON_INVALID_INPUT;

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ResponseBody.error(
                errorCode.getStatusCode(),
                errorCode.getCode(),
                errorCode.getMessage(),
                ex.getBindingResult()
            ));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ResponseBody<Void>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        ErrorCode errorCode = ErrorCode.COMMON_INVALID_INPUT;

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ResponseBody.error(
                errorCode.getStatusCode(),
                errorCode.getCode(),
                errorCode.getMessage()
            ));
    }
    @ExceptionHandler(HandlerMethodValidationException.class)
    protected ResponseEntity<ResponseBody<Void>> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        ErrorCode errorCode = ErrorCode.COMMON_INVALID_INPUT;

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ResponseBody.error(
                errorCode.getStatusCode(),
                errorCode.getCode(),
                errorCode.getMessage()
            ));
    }

}
