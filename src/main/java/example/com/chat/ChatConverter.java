package example.com.chat;

import example.com.chat.domain.Chat;
import example.com.chat.domain.Chatroom;
import example.com.chat.dto.ChatResponse;

public class ChatConverter {
    public static ChatResponse.ChatroomCreateResultDto toChatroomCreateResultDto(Chatroom chatroom, Long targetMemberId) {

        return ChatResponse.ChatroomCreateResultDto.builder()
                .chatroomId(chatroom.getId())
                .chatroomType(chatroom.getChatroomType())
                .uuid(chatroom.getUuid())
                .postUrl(chatroom.getPostUrl())
                .targetMemberId(targetMemberId)
                .build();
    }

    public static ChatResponse.ChatCreateResultDto toChatCreateResultDto(Chat chat) {

        return ChatResponse.ChatCreateResultDto.builder()
                .chatId(chat.getId())
                .senderName(chat.getFromMember().getName())
                .message(chat.getContents())
                .createdAt(chat.getCreatedAt())
                .build();
    }
}
