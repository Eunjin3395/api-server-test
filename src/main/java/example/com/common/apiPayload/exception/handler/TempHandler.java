package example.com.common.apiPayload.exception.handler;

import example.com.common.apiPayload.code.BaseErrorCode;
import example.com.common.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {
    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
