package example.com.chat;

import example.com.chat.domain.Chat;
import example.com.chat.domain.Chatroom;
import example.com.chat.dto.ChatResponse;
import example.com.member.domain.Member;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

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
                .senderId(chat.getFromMember().getId())
                .senderProfileImg(chat.getFromMember().getProfileImg())
                .senderName(chat.getFromMember().getName())
                .message(chat.getContents())
                .createdAt(chat.getCreatedAt())
                .build();
    }

    public static ChatResponse.ChatMessageListDto toChatMessageListDto(Page<Chat> chat, Member member) {
        List<ChatResponse.ChatMessageDto> chatMessageDtoList = chat.stream()
                .map(chatElement -> {
                    return toChatMessageDto(chatElement, member);
                }).collect(Collectors.toList());

        return ChatResponse.ChatMessageListDto.builder()
                .chatMessageDtoList(chatMessageDtoList)
                .total_page(chat.getTotalPages())
                .total_elements(chat.getTotalElements())
                .list_size(chatMessageDtoList.size())
                .is_first(chat.isFirst())
                .is_last(chat.isLast())
                .build();
    }

    public static ChatResponse.ChatMessageDto toChatMessageDto(Chat chat, Member member) {

        return ChatResponse.ChatMessageDto.builder()
                .chatId(chat.getId())
                .senderName(chat.getFromMember().getName())
                .senderProfileImg(chat.getFromMember().getProfileImg())
                .message(chat.getContents())
                .isMine(chat.getFromMember().equals(member))
                .createdAt(chat.getCreatedAt())
                .build();

    }

    public static ChatResponse.ChatroomEnterResultDto toChatroomEnterResultDto(Member targetMember) {
        return ChatResponse.ChatroomEnterResultDto.builder()
                .memberId(targetMember.getId())
                .memberProfileImg(targetMember.getProfileImg())
                .name(targetMember.getName())
                .build();
    }
}
