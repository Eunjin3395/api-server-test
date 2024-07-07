package example.com.chat;

import example.com.auth.security.SecurityUtil;
import example.com.chat.domain.Chatroom;
import example.com.chat.dto.ChatRequest;
import example.com.chat.dto.ChatResponse;
import example.com.common.apiPayload.ApiResponse;
import example.com.member.MemberService;
import example.com.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
@Tag(name = "Chat", description = "Chat 관련 API")
public class ChatController {
    private final MemberService memberService;
    private final ChatService chatService;

    @Operation(summary = "채팅방 uuid 조회 API", description = "회원이 속한 채팅방의 uuid를 조회하는 API 입니다.")
    @GetMapping("/member/chatroom/uuid")
    public ApiResponse<List<String>> getChatroom() {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());
        List<String> chatroomUuids = chatService.getChatroomUuids(member);
        return ApiResponse.onSuccess(chatroomUuids);
    }

    @Operation(summary = "채팅방 생성 API", description = "채팅방을 생성하는 API 입니다.")
    @PostMapping("/chatroom")
    public ApiResponse<ChatResponse.ChatCreateResultDto> createChatroom(
            @RequestBody @Valid ChatRequest.ChatCreateRequest request
    ) {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());
        Chatroom chatroom = chatService.createChatroom(request, member);
        return ApiResponse.onSuccess(ChatConverter.toChatCreateResultDto(chatroom, request.getTargetMemberId()));
    }


}
