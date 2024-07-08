package example.com.chat.dto;

import example.com.chat.domain.ChatroomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ChatResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatCreateResultDto {
        Long chatroomId;
        String uuid;
        ChatroomType chatroomType;
        String postUrl;
        Long targetMemberId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatroomViewDto {
        Long chatroomId;
        String uuid;
        String targetMemberImg;
        String targetMemberName;
        String lastMsg;
        LocalDateTime lastMsgTime;
        Integer notReadMsgCnt;
    }
}
