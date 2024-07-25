package example.com.chat;

import example.com.chat.domain.Chat;
import example.com.chat.domain.Chatroom;
import example.com.chat.domain.MemberChatroom;
import example.com.chat.dto.ChatResponse;
import example.com.chat.repository.ChatRepository;
import example.com.chat.repository.ChatroomRepository;
import example.com.chat.repository.MemberChatroomRepository;
import example.com.common.apiPayload.code.status.ErrorStatus;
import example.com.common.apiPayload.exception.handler.ChatHandler;
import example.com.member.domain.Member;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatQueryService {

    private final ChatroomRepository chatroomRepository;
    private final MemberChatroomRepository memberChatroomRepository;
    private final ChatRepository chatRepository;

    private final static int PAGE_SIZE = 20;

    /**
     * 해당 회원의 ACTIVE한 채팅방의 uuid list를 리턴
     *
     * @param member
     * @return
     */
    @Transactional(readOnly = true)
    public List<String> getChatroomUuids(Member member) {
        return chatroomRepository.findActiveChatroomUuidsByMemberId(member.getId());
    }

    /**
     * 채팅방 목록 조회
     *
     * @param member
     * @return
     */
    @Transactional(readOnly = true)
    public List<ChatResponse.ChatroomViewDto> getChatroomList(Member member) {
        // 현재 참여중인 memberChatroom을 각 memberChatroom에 속한 chat의 마지막 createdAt 기준 desc 정렬해 조회
        List<MemberChatroom> activeMemberChatroom = memberChatroomRepository.findActiveMemberChatroomOrderByLastChat(
            member.getId());

        List<ChatResponse.ChatroomViewDto> chatroomViewDtoList = activeMemberChatroom.stream()
            .map(memberChatroom -> {
                // 채팅 상대 회원 조회
                Member targetMember = memberChatroomRepository.findTargetMemberByChatroomIdAndMemberId(
                    memberChatroom.getChatroom().getId(), member.getId());
                Chatroom chatroom = memberChatroom.getChatroom();

                // 가장 마지막 대화 조회
                Chat lastChat = chatRepository.findFirstByChatroomIdOrderByCreatedAtDesc(
                    chatroom.getId());

                // 내가 읽지 않은 메시지 개수 조회
                Integer unReadCnt = chatRepository.countUnreadChats(
                    chatroom.getId(), memberChatroom.getId());

                // ISO 8601 형식의 문자열로 변환
                String lastAtIoString = lastChat.getCreatedAt()
                    .format(DateTimeFormatter.ISO_DATE_TIME);

                return ChatResponse.ChatroomViewDto.builder()
                    .chatroomId(chatroom.getId())
                    .uuid(chatroom.getUuid())
                    .targetMemberImg(targetMember.getProfileImg())
                    .targetMemberName(targetMember.getName())
                    .lastMsg(lastChat.getContents())
                    .lastMsgAt(lastAtIoString)
                    .notReadMsgCnt(unReadCnt)
                    .build();

            })
            .collect(Collectors.toList());

        return chatroomViewDtoList;
    }

    /**
     * chatroomUuid에 해당하는 채팅방의 메시지 내역 조회, 페이징 포함
     *
     * @param chatroomUuid
     * @param member
     * @param cursor
     * @return
     */
    @Transactional(readOnly = true)
    public Slice<Chat> getChatMessagesByCursor(String chatroomUuid, Member member, Long cursor) {
        // chatroom 엔티티 조회 및 해당 회원의 채팅방이 맞는지 검증
        Chatroom chatroom = chatroomRepository.findByUuid(chatroomUuid)
            .orElseThrow(() -> new ChatHandler(ErrorStatus.CHATROOM_NOT_EXIST));

        MemberChatroom memberChatroom = memberChatroomRepository.findByMemberIdAndChatroomId(
                member.getId(), chatroom.getId())
            .orElseThrow(() -> new ChatHandler(ErrorStatus.CHATROOM_ACCESS_DENIED));

        // 해당 회원이 퇴장한 채팅방은 아닌지도 나중에 검증 추가하기

        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE);

        // requestParam으로 cursor가 넘어온 경우
        if (cursor != null) {
            return chatRepository.findChatsByCursor(cursor, chatroom.getId(), pageRequest);
        } else { // cursor가 넘어오지 않은 경우 = 해당 chatroom의 가장 최근 chat을 조회하는 요청
            return chatRepository.findRecentChats(chatroom.getId(), memberChatroom.getId());
        }
    }

}
