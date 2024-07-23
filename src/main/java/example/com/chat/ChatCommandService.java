package example.com.chat;

import example.com.chat.domain.Chat;
import example.com.chat.domain.Chatroom;
import example.com.chat.domain.ChatroomStatus;
import example.com.chat.domain.ChatroomType;
import example.com.chat.domain.MemberChatroom;
import example.com.chat.dto.ChatRequest;
import example.com.chat.repository.ChatRepository;
import example.com.chat.repository.ChatroomRepository;
import example.com.chat.repository.MemberChatroomRepository;
import example.com.common.apiPayload.code.status.ErrorStatus;
import example.com.common.apiPayload.exception.handler.ChatHandler;
import example.com.member.domain.Member;
import example.com.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatCommandService {

    private final MemberRepository memberRepository;
    private final ChatroomRepository chatroomRepository;
    private final MemberChatroomRepository memberChatroomRepository;
    private final ChatRepository chatRepository;

    private final static int PAGE_SIZE = 20;

    /**
     * 대상 회원과의 채팅방 생성
     *
     * @param request
     * @param member
     * @return
     */
    @Transactional
    public Chatroom createChatroom(ChatRequest.ChatroomCreateRequest request, Member member) {
        // 채팅 대상 회원의 존재 여부 검증
        Member targetMember = memberRepository.findById(request.getTargetMemberId())
            .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_TARGET_NOT_FOUND));

        // chatroom 엔티티 생성
        Chatroom chatroom = null;
        String uuid = UUID.randomUUID().toString();
        if (request.getChatroomType().equals(ChatroomType.FRIEND.toString())) { // 친구목록에서 시작된 채팅인 경우
            chatroom = Chatroom.builder()
                .uuid(uuid)
                .chatroomType(ChatroomType.FRIEND)
                .postUrl(null)
                .startMember(null)
                .build();
        } else { // 특정 글을 보고 시작된 채팅인 경우
            chatroom = Chatroom.builder()
                .uuid(uuid)
                .chatroomType(ChatroomType.POST)
                .postUrl(request.getPostUrl())
                .startMember(member)
                .build();
        }
        Chatroom savedChatroom = chatroomRepository.save(chatroom);

        // MemberChatroom 엔티티 생성 및 연관관계 매핑
        // 나의 MemberChatroom 엔티티
        MemberChatroom memberChatroom = MemberChatroom.builder()
            .chatroomStatus(ChatroomStatus.ACTIVE)
            .lastViewDateTime(LocalDateTime.now())
            .chatroom(chatroom)
            .build();
        memberChatroom.setMember(member);
        memberChatroomRepository.save(memberChatroom);

        // 상대방의 MemberChatroom 엔티티
        MemberChatroom targetMemberChatroom = MemberChatroom.builder()
            .chatroomStatus(ChatroomStatus.ACTIVE)
            .lastViewDateTime(null)
            .chatroom(chatroom)
            .build();
        targetMemberChatroom.setMember(targetMember);
        memberChatroomRepository.save(targetMemberChatroom);

        return savedChatroom;
    }


    /**
     * 채팅 등록 메소드
     *
     * @param request
     * @param member
     * @return
     */
    @Transactional
    public Chat addChat(ChatRequest.ChatCreateRequest request, String chatroomUuid, Member member) {
        // 채팅방 조회 및 존재 여부 검증
        Chatroom chatroom = chatroomRepository.findByUuid(chatroomUuid)
            .orElseThrow(() -> new ChatHandler(ErrorStatus.CHATROOM_NOT_EXIST));

        // 해당 채팅방이 회원의 것이 맞는지 검증
        MemberChatroom memberChatroom = memberChatroomRepository.findByMemberIdAndChatroomId(
                member.getId(), chatroom.getId())
            .orElseThrow(() -> new ChatHandler(ErrorStatus.CHATROOM_ACCESS_DENIED));

        // 해당 회원이 이미 나간 채팅방인지 검증
        if (memberChatroom.getChatroomStatus().equals(ChatroomStatus.INACTIVE)) {
            throw new ChatHandler(ErrorStatus.CHATROOM_ACCESS_DENIED);
        }

        // chat 엔티티 생성
        Chat chat = Chat.builder()
            .contents(request.getMessage())
            .chatroom(chatroom)
            .fromMember(member)
            .build();

        // MemberChatroom의 lastViewDate 업데이트
        Chat savedChat = chatRepository.save(chat);
        memberChatroom.updateLastViewDateTime(savedChat.getCreatedAt());

        return savedChat;
    }


    /**
     * chatroomUuid에 해당하는 채팅방에 입장 처리: lastViewDate 업데이트 및 메시지 읽음 처리 후 상대 회원 엔티티를 리턴
     *
     * @param chatroomUuid
     * @param member
     * @return
     */
    @Transactional
    public Member enterChatroom(String chatroomUuid, Member member) {
        // chatroom 엔티티 조회 및 해당 회원의 채팅방이 맞는지 검증
        Chatroom chatroom = chatroomRepository.findByUuid(chatroomUuid)
            .orElseThrow(() -> new ChatHandler(ErrorStatus.CHATROOM_NOT_EXIST));

        MemberChatroom memberChatroom = memberChatroomRepository.findByMemberIdAndChatroomId(
                member.getId(), chatroom.getId())
            .orElseThrow(() -> new ChatHandler(ErrorStatus.CHATROOM_ACCESS_DENIED));

        // 해당 회원이 퇴장한 채팅방은 아닌지도 나중에 검증 추가하기

        // 해당 채팅방의 lastViewDateTime 업데이트
        memberChatroom.setLastViewDateTime(LocalDateTime.now());

        return memberChatroomRepository.findTargetMemberByChatroomIdAndMemberId(chatroom.getId(),
            member.getId());
    }
}
