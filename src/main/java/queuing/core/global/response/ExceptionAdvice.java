package queuing.core.global.response;

import java.time.DateTimeException;
import java.time.Instant;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ExceptionAdvice {
    private static final Logger log = LoggerFactory.getLogger(ExceptionAdvice.class);

    private static final Random random = new Random();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*/?";
    private static final int ERROR_KEY_LENGTH = 8;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        final String message = e.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        log.warn(message);

        return ResponseEntity.badRequest()
            .body(ResponseBody.error(e.getClass().getName(), message));
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<?> handleDateTimeException(final DateTimeException e) {
        log.warn(e.getMessage());

        return ResponseEntity.badRequest()
            .body(ResponseBody.error());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.warn(e.getMessage());

        return ResponseEntity.badRequest()
            .body(ResponseBody.error());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(final AuthenticationException e) {
        log.warn(e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ResponseBody.error());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxUploadSizeExceededException(final MaxUploadSizeExceededException e) {
        log.warn(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseBody.error());
    }

    @ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<?> handleNoHandlerFoundException(final Exception e) {
        return ResponseEntity.badRequest()
            .body(ResponseBody.error());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(final NoResourceFoundException e) {
        return ResponseEntity.badRequest()
            .body(ResponseBody.error());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(final RuntimeException e) {
        final StringBuilder sb = new StringBuilder();
        sb.append(Instant.now().toEpochMilli()).append(":");
        for (int i = 0; i < ERROR_KEY_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        log.error("{}\n UNEXPECTED EXCEPTION BUSINESS CODE: {}\n UNEXPECTED EXCEPTION CLASS TYPE: {}", e.getMessage(), sb, e.getClass());

        return ResponseEntity.internalServerError()
            .body(ResponseBody.error(e.getClass().getName(), sb.toString()));
    }
}
