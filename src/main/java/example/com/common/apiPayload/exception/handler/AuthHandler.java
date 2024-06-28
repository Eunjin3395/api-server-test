package example.com.common.apiPayload.exception.handler;

import example.com.common.apiPayload.code.BaseErrorCode;
import example.com.common.apiPayload.exception.GeneralException;

public class AuthHandler extends GeneralException {
    public AuthHandler(BaseErrorCode code) {
        super(code);
    }
}
