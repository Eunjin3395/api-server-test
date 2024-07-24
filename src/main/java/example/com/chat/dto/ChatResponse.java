package example.com.chat.dto;

import example.com.chat.domain.ChatroomType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        String lastMsgAt;
        Integer notReadMsgCnt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatCreateResultDto {

        Long senderId;
        String senderName;
        String senderProfileImg;
        String message;
        String createdAt;
        Long timestamp;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessageListDto {

        List<ChatMessageDto> chatMessageDtoList;
        Integer list_size;
        Boolean has_next;
        String next_cursor;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessageDto {

        //Long chatId;
        Long senderId;
        String senderName;
        String senderProfileImg;
        String message;
        String createdAt;
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatroomMessageDto {

        String chatroomUuid;
        List<ChatMessageDto> chatMessageDtoList;
    }

}
