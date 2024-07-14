package example.com.chat;

import example.com.chat.domain.Chat;
import example.com.chat.domain.Chatroom;
import example.com.chat.domain.ChatroomStatus;
import example.com.chat.domain.MemberChatroom;
import example.com.chat.dto.ChatResponse;
import example.com.chat.repository.ChatRepository;
import example.com.chat.repository.ChatroomRepository;
import example.com.chat.repository.MemberChatroomRepository;
import example.com.common.apiPayload.code.status.ErrorStatus;
import example.com.common.apiPayload.exception.handler.ChatHandler;
import example.com.member.domain.Member;
import example.com.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatQueryService {
    private final MemberRepository memberRepository;
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
        // 현재 ACTIVE한 memberChatroom만 필터링
        List<MemberChatroom> activeMemberChatroom = member.getMemberChatroomList().stream()
                .filter(memberChatroom -> memberChatroom.getChatroomStatus().equals(ChatroomStatus.ACTIVE))
                .toList();

        List<ChatResponse.ChatroomViewDto> chatroomViewDtoList = activeMemberChatroom.stream().map(memberChatroom -> {
                    // 채팅 상대 회원 조회
                    Member targetMember = memberChatroomRepository.findTargetMemberByChatroomIdAndMemberId(memberChatroom.getChatroom().getId(), member.getId());
                    Chatroom chatroom = memberChatroom.getChatroom();

                    // 가장 마지막 대화 조회
                    Chat lastChat = chatRepository.findFirstByChatroomIdOrderByCreatedAtDesc(chatroom.getId());

                    // 내가 읽지 않은 메시지 개수 조회
                    LocalDateTime lastViewDateTime = memberChatroom.getLastViewDateTime();
                    Integer unReadCnt = chatRepository.countChatsByChatroomIdAndFromMemberIdAfterLastViewDateTime(chatroom.getId(), member.getId(), lastViewDateTime);

                    // ISO 8601 형식의 문자열로 변환
                    String lastAtIoString = lastChat.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME);
                    
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
     * @param pageIdx
     * @return
     */
    @Transactional(readOnly = true)
    public Page<Chat> getChatMessages(String chatroomUuid, Member member, Integer pageIdx) {
        // chatroom 엔티티 조회 및 해당 회원의 채팅방이 맞는지 검증
        Chatroom chatroom = chatroomRepository.findByUuid(chatroomUuid)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHATROOM_NOT_EXIST));

        MemberChatroom memberChatroom = memberChatroomRepository.findByMemberIdAndChatroomId(member.getId(), chatroom.getId())
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHATROOM_ACCESS_DENIED));

        // 해당 회원이 퇴장한 채팅방은 아닌지도 나중에 검증 추가하기

        PageRequest pageRequest = PageRequest.of(pageIdx, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));

        return chatRepository.findAllByChatroom(chatroom, pageRequest);
    }

    /**
     * @param member
     * @return
     */
    @Transactional(readOnly = true)
    public List<ChatResponse.ChatroomMessageDto> getAllChatroomMessage(Member member) {
        // 현재 ACTIVE한 memberChatroom만 필터링
        List<MemberChatroom> activeMemberChatroom = member.getMemberChatroomList().stream()
                .filter(memberChatroom -> memberChatroom.getChatroomStatus().equals(ChatroomStatus.ACTIVE))
                .toList();


        List<ChatResponse.ChatroomMessageDto> chatroomMessageDtoList = activeMemberChatroom.stream().map(memberChatroom -> {
                    // 해당 chatroom의 최근 20개 채팅 내역 조회
                    Pageable pageable = PageRequest.of(0, 20);
                    List<Chat> top20ChatList = chatRepository.findTop20ByChatroomIdOrderByCreatedAtDesc(memberChatroom.getChatroom().getId(), pageable);

                    // 날짜 오름차순으로 변경
                    Collections.reverse(top20ChatList);

                    // chat -> dto 변환
                    List<ChatResponse.ChatMessageDto> chatMessageDtoList = top20ChatList.stream().map(chat -> {
                        // ISO 8601 형식의 문자열로 변환
                        String createdAtIoString = chat.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME);

                        return ChatResponse.ChatMessageDto.builder()
                                .senderId(chat.getFromMember().getId())
                                .senderName(chat.getFromMember().getName())
                                .senderProfileImg(chat.getFromMember().getProfileImg())
                                .message(chat.getContents())
                                .createdAt(createdAtIoString)
                                .build();
                    }).collect(Collectors.toList());

                    return ChatResponse.ChatroomMessageDto.builder()
                            .chatroomUuid(memberChatroom.getChatroom().getUuid())
                            .chatMessageDtoList(chatMessageDtoList)
                            .build();

                })
                .collect(Collectors.toList());

        return chatroomMessageDtoList;
    }
}
