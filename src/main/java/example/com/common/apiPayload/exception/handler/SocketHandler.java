package example.com.common.apiPayload.exception.handler;

import example.com.common.apiPayload.code.BaseErrorCode;
import example.com.common.apiPayload.exception.GeneralException;

public class SocketHandler extends GeneralException {
    public SocketHandler(BaseErrorCode code) {
        super(code);
    }
}
