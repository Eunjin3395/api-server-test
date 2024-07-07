package example.com.common.apiPayload.exception.handler;

import example.com.common.apiPayload.code.BaseErrorCode;
import example.com.common.apiPayload.exception.GeneralException;

public class ChatHandler extends GeneralException {
    public ChatHandler(BaseErrorCode code) {
        super(code);
    }
}
