package example.com.chat;

import example.com.chat.domain.Chat;
import example.com.chat.domain.Chatroom;
import example.com.chat.dto.ChatResponse;
import example.com.member.domain.Member;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Slice;

public class ChatConverter {

    public static ChatResponse.ChatroomCreateResultDto toChatroomCreateResultDto(Chatroom chatroom,
        Long targetMemberId) {

        return ChatResponse.ChatroomCreateResultDto.builder()
            .chatroomId(chatroom.getId())
            .chatroomType(chatroom.getChatroomType())
            .uuid(chatroom.getUuid())
            .postUrl(chatroom.getPostUrl())
            .targetMemberId(targetMemberId)
            .build();
    }

    public static ChatResponse.ChatCreateResultDto toChatCreateResultDto(Chat chat) {
        // ISO 8601 형식의 문자열로 변환
        String createdAtIoString = chat.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME);

        return ChatResponse.ChatCreateResultDto.builder()
            .senderId(chat.getFromMember().getId())
            .senderProfileImg(chat.getFromMember().getProfileImg())
            .senderName(chat.getFromMember().getName())
            .message(chat.getContents())
            .createdAt(createdAtIoString)
            .timestamp(chat.getTimestamp())
            .build();
    }

    public static ChatResponse.ChatMessageListDto toChatMessageListDto(Slice<Chat> chat) {
        List<ChatResponse.ChatMessageDto> chatMessageDtoList = chat.stream()
            .map(ChatConverter::toChatMessageDto).collect(Collectors.toList());

        return ChatResponse.ChatMessageListDto.builder()
            .chatMessageDtoList(chatMessageDtoList)
            .list_size(chatMessageDtoList.size())
            .has_next(chat.hasNext())
            .next_cursor(chat.hasNext() ? chat.getContent().get(0).getTimestamp()
                : null) // next cursor를 현재 chat list의 가장 오래된 chat의 timestamp로 주기
            .build();
    }

    public static ChatResponse.ChatMessageDto toChatMessageDto(Chat chat) {
        // ISO 8601 형식의 문자열로 변환
        String createdAtIoString = chat.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME);

        return ChatResponse.ChatMessageDto.builder()
            .senderId(chat.getFromMember().getId())
            .senderName(chat.getFromMember().getName())
            .senderProfileImg(chat.getFromMember().getProfileImg())
            .message(chat.getContents())
            .createdAt(createdAtIoString)
            .timestamp(chat.getTimestamp())
            .build();

    }

    public static ChatResponse.ChatroomEnterResultDto toChatroomEnterResultDto(
        Member targetMember) {
        return ChatResponse.ChatroomEnterResultDto.builder()
            .memberId(targetMember.getId())
            .memberProfileImg(targetMember.getProfileImg())
            .name(targetMember.getName())
            .build();
    }
}
