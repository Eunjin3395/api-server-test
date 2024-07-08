package example.com.chat;

import example.com.chat.domain.*;
import example.com.chat.dto.ChatRequest;
import example.com.chat.dto.ChatResponse;
import example.com.chat.repository.ChatRepository;
import example.com.chat.repository.ChatroomRepository;
import example.com.chat.repository.MemberChatroomRepository;
import example.com.common.apiPayload.code.status.ErrorStatus;
import example.com.common.apiPayload.exception.handler.ChatHandler;
import example.com.member.domain.Member;
import example.com.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MemberRepository memberRepository;
    private final ChatroomRepository chatroomRepository;
    private final MemberChatroomRepository memberChatroomRepository;
    private final ChatRepository chatRepository;

    /**
     * 해당 회원의 ACTIVE한 채팅방의 uuid list를 리턴
     *
     * @param member
     * @return
     */
    public List<String> getChatroomUuids(Member member) {
        return chatroomRepository.findActiveChatroomUuidsByMemberId(member.getId());

    }

    /**
     * 대상 회원과의 채팅방 생성
     *
     * @param request
     * @param member
     * @return
     */
    public Chatroom createChatroom(ChatRequest.ChatCreateRequest request, Member member) {
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

    public List<ChatResponse.ChatroomViewDto> getChatroomList(Member member) {
        // 현재 ACTIVE한 memberChatroom만 필터링
        List<MemberChatroom> activeMemberChatroom = member.getMemberChatroomList().stream()
                .filter(memberChatroom -> memberChatroom.getChatroomStatus().equals(ChatroomStatus.ACTIVE))
                .toList();

        List<ChatResponse.ChatroomViewDto> chatroomViewDtoList = activeMemberChatroom.stream().map(memberChatroom -> {
                    // 채팅 상대 회원 조회
                    Member targetMember = memberChatroomRepository.findMemberByChatroomIdAndMemberIdNot(memberChatroom.getChatroom().getId(), member.getId());
                    Chatroom chatroom = memberChatroom.getChatroom();

                    // 가장 마지막 대화 조회
                    Chat lastChat = chatRepository.findFirstByChatroomIdOrderByCreatedAtDesc(chatroom.getId());

                    // 내가 읽지 않은 메시지 개수 조회
                    Integer unReadCnt = chatRepository.countUnreadChatsByChatroomIdAndFromMemberId(chatroom.getId(), member.getId());

                    return ChatResponse.ChatroomViewDto.builder()
                            .chatroomId(chatroom.getId())
                            .uuid(chatroom.getUuid())
                            .targetMemberImg(targetMember.getProfileImg())
                            .targetMemberName(targetMember.getName())
                            .lastMsg(lastChat.getContents())
                            .lastMsgTime(lastChat.getCreatedAt())
                            .notReadMsgCnt(unReadCnt)
                            .build();

                })
                .collect(Collectors.toList());


        return chatroomViewDtoList;
    }
}
