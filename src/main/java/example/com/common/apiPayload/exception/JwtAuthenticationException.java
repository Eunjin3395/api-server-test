package example.com.common.apiPayload.exception;

import org.springframework.security.core.AuthenticationException;
import example.com.common.apiPayload.code.status.ErrorStatus;

public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException(ErrorStatus code) {
        super(code.name());
    }

}
