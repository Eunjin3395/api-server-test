package example.com.chat;

import example.com.chat.domain.Chatroom;
import example.com.chat.dto.ChatResponse;

public class ChatConverter {
    public static ChatResponse.ChatCreateResultDto toChatCreateResultDto(Chatroom chatroom, Long targetMemberId) {

        return ChatResponse.ChatCreateResultDto.builder()
                .chatroomId(chatroom.getId())
                .chatroomType(chatroom.getChatroomType())
                .uuid(chatroom.getUuid())
                .postUrl(chatroom.getPostUrl())
                .targetMemberId(targetMemberId)
                .build();
    }
}
