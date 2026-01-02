package queuing.core.global.security.exception;

import java.io.Serial;

import org.springframework.security.access.AccessDeniedException;

public class InvalidPrincipalException extends AccessDeniedException {
    @Serial
    private static final long serialVersionUID = 6352653927830527850L;

    public InvalidPrincipalException() {
        super("Unauthorized access");
    }
}
