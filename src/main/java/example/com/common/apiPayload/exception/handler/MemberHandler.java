package example.com.common.apiPayload.exception.handler;

import example.com.common.apiPayload.code.BaseErrorCode;
import example.com.common.apiPayload.exception.GeneralException;

public class MemberHandler extends GeneralException {
    public MemberHandler(BaseErrorCode code) {
        super(code);
    }
}
