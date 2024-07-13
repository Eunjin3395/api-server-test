package example.com.chat.dto;

import example.com.chat.domain.ChatroomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ChatResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatroomCreateResultDto {
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatCreateResultDto {
        Long chatId;
        Long senderId;
        String senderName;
        String senderProfileImg;
        String message;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessageListDto {
        List<ChatMessageDto> chatMessageDtoList;
        Integer list_size;
        Integer total_page;
        Long total_elements;
        Boolean is_first;
        Boolean is_last;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessageDto {
        Long chatId;
        Long senderId;
        String senderName;
        String senderProfileImg;
        String message;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatroomEnterResultDto {
        Long memberId;
        String name;
        String memberProfileImg;
    }

}
