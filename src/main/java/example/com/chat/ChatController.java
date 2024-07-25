package example.com.chat;

import example.com.auth.security.SecurityUtil;
import example.com.chat.domain.Chat;
import example.com.chat.domain.Chatroom;
import example.com.chat.dto.ChatRequest;
import example.com.chat.dto.ChatResponse;
import example.com.common.apiPayload.ApiResponse;
import example.com.member.MemberService;
import example.com.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
@Tag(name = "Chat", description = "Chat 관련 API")
public class ChatController {

    private final MemberService memberService;
    private final ChatCommandService chatCommandService;
    private final ChatQueryService chatQueryService;

    @Operation(summary = "채팅방 uuid 조회 API", description = "회원이 속한 채팅방의 uuid를 조회하는 API 입니다.")
    @GetMapping("/member/chatroom/uuid")
    public ApiResponse<List<String>> getChatroomUuid() {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());
        List<String> chatroomUuids = chatQueryService.getChatroomUuids(member);
        return ApiResponse.onSuccess(chatroomUuids);
    }

    @Operation(summary = "채팅방 생성 API", description = "채팅방을 생성하는 API 입니다.")
    @PostMapping("/chatroom")
    public ApiResponse<ChatResponse.ChatroomCreateResultDto> createChatroom(
        @RequestBody @Valid ChatRequest.ChatroomCreateRequest request
    ) {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());
        Chatroom chatroom = chatCommandService.createChatroom(request, member);
        return ApiResponse.onSuccess(
            ChatConverter.toChatroomCreateResultDto(chatroom, request.getTargetMemberId()));
    }

    @Operation(summary = "채팅방 목록 조회 API", description = "회원이 속한 채팅방 목록을 조회하는 API 입니다.")
    @GetMapping("/member/chatroom")
    public ApiResponse<List<ChatResponse.ChatroomViewDto>> getChatroom() {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());

        return ApiResponse.onSuccess(chatQueryService.getChatroomList(member));
    }

    @Operation(summary = "채팅 메시지 등록 API", description = "새로운 채팅 메시지를 등록하는 API 입니다.")
    @PostMapping("/chat/{chatroomUuid}")
    public ApiResponse<ChatResponse.ChatCreateResultDto> addChat(
        @PathVariable(name = "chatroomUuid") String chatroomUuid,
        @RequestBody ChatRequest.ChatCreateRequest request
    ) {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());
        Chat chat = chatCommandService.addChat(request, chatroomUuid, member);

        return ApiResponse.onSuccess(ChatConverter.toChatCreateResultDto(chat));
    }

    @Operation(summary = "채팅방 입장 API", description = "특정 채팅방에 입장하는 API 입니다. 채팅 상대의 id, 프로필 이미지, 닉네임을 리턴합니다.")
    @GetMapping("/chat/{chatroomUuid}")
    public ApiResponse<ChatResponse.ChatroomEnterResultDto> enterChatroom(
        @PathVariable(name = "chatroomUuid") String chatroomUuid
    ) {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());
        Member targetMember = chatCommandService.enterChatroom(chatroomUuid, member);

        return ApiResponse.onSuccess(ChatConverter.toChatroomEnterResultDto(targetMember));
    }

    @Operation(summary = "채팅 내역 조회 API", description = "특정 채팅방의 메시지 내역을 조회하는 API 입니다.\n\n" +
        "cursor 파라미터를 보내면, 해당 timestamp 이전에 전송된 메시지 최대 20개를 조회합니다.\n\n" +
        "cursor 파라미터를 보내지 않으면, 해당 채팅방의 가장 최근 메시지 내역을 조회합니다.")
    @GetMapping("/chat/{chatroomUuid}/messages")
    @Parameter(name = "cursor", description = "페이징을 위한 커서, 13자리 timestamp integer를 보내주세요. (UTC 기준)")
    public ApiResponse<Object> getChatMessages(
        @PathVariable(name = "chatroomUuid") String chatroomUuid,
        @RequestParam(name = "cursor", required = false) Long cursor
    ) {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());
        Slice<Chat> chatMessages = chatQueryService.getChatMessagesByCursor(chatroomUuid, member,
            cursor);

        return ApiResponse.onSuccess(ChatConverter.toChatMessageListDto(chatMessages));
    }

    @Operation(summary = "채팅 메시지 읽음 처리 API", description = "특정 채팅방의 메시지를 읽음 처리하는 API 입니다.")
    @GetMapping("/chatroom/{chatroomUuid}/read")
    @Parameter(name = "timestamp", description = "특정 메시지를 읽음 처리하는 경우, 그 메시지의 timestamp를 함께 보내주세요.")
    public ApiResponse<String> readChatMessage(
        @PathVariable(name = "chatroomUuid") String chatroomUuid,
        @RequestParam(name = "timestamp", required = false) Long timestamp
    ) {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());

        chatCommandService.readChatMessages(chatroomUuid, timestamp, member);
        return ApiResponse.onSuccess("채팅 메시지 읽음 처리 성공");
    }
}
