package example.com.common.apiPayload.code.status;

import example.com.common.apiPayload.code.BaseErrorCode;
import example.com.common.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 에러
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "테스트"),

    // 인증 관련 에러
    INVALID_LOGINTYPE(HttpStatus.BAD_REQUEST, "AUTH4001", "유효하지 않은 로그인 타입입니다."),
    INVALID_TOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "AUTH4002", "토큰이 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "AUTH4003", "리프레쉬 토큰이 유효하지 않습니다. 다시 로그인 해주세요"),
    EXPIRED_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "AUTH4004", "기존 토큰이 만료되었습니다. 토큰을 재발급해주세요."),
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "AUTH4005", "로그인 후 이용가능합니다. 토큰을 입력해 주세요"),
    INACTIVE_MEMBER(HttpStatus.NOT_FOUND, "AUTH4006", "탈퇴한 사용자 입니다."),


    // 회원 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4001", "사용자를 찾을 수 없습니다."),
    MEMBER_EXISTS(HttpStatus.BAD_REQUEST, "MEMBER4002", "이미 가입된 사용자 입니다."),

    // 채팅 관련 에러
    CHAT_TARGET_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT4001", "채팅 대상 회원을 찾을 수 없습니다."),
    CHATROOM_NOT_EXIST(HttpStatus.NOT_FOUND, "CHAT4002", "채팅방을 찾을 수 없습니다."),
    CHATROOM_ACCESS_DENIED(HttpStatus.BAD_REQUEST, "CHAT4003", "접근할 수 없는 채팅방 입니다."),
    CHAT_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT4004", "해당 메시지를 찾을 수 없습니다"),


    // 소켓 api 관련 에러
    SOCKET_API_RESPONSE_ERR(HttpStatus.INTERNAL_SERVER_ERROR, "SOCKET4001",
        "socket서버 api 요청에 실패했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
            .message(message)
            .code(code)
            .isSuccess(false)
            .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
            .message(message)
            .code(code)
            .isSuccess(false)
            .httpStatus(httpStatus)
            .build();
    }

}
