package example.com.member;

import example.com.auth.AuthService;
import example.com.auth.dto.AuthResponse;
import example.com.auth.security.SecurityUtil;
import example.com.common.apiPayload.ApiResponse;
import example.com.common.apiPayload.code.status.SuccessStatus;
import example.com.member.domain.Friend;
import example.com.member.domain.Member;
import example.com.member.dto.MemberRequest;
import example.com.member.dto.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/member")
@Tag(name = "Member", description = "Member 관련 API")
public class MemberController {

    private final AuthService authService;
    private final MemberService memberService;

    @PostMapping("/signin/{loginType}")
    @Operation(summary = "회원가입 API", description = "소셜 계정 기반 회원 가입 API 입니다.")
    @Parameters(value = {
            @Parameter(name = "loginType", description = "소셜 로그인 타입으로, KAKAO 또는 APPLE을 입력해야 합니다.")
    })
    public ApiResponse<AuthResponse.loginDto> signin(@RequestBody @Valid MemberRequest.signinRequest request,
                                                     @PathVariable(name = "loginType") String loginType
    ) {
        Member member = memberService.join(request, loginType);
        AuthResponse.loginDto loginDto = authService.login(member.getSocialId(), member.getLoginType().toString());

        return ApiResponse.of(SuccessStatus.JOIN_SUCCESS, loginDto);
    }

    @GetMapping("/test")
    @Operation(summary = "테스트용 회원 정보 조회 API", description = "jwt 테스트용, 로그인한 회원 정보를 조회하는 API 입니다.")
    public ApiResponse<MemberResponse.myInfoDto> getMyInfo(Authentication authentication) {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());

        MemberResponse.myInfoDto response = MemberResponse.myInfoDto.builder()
                .socialId(member.getSocialId())
                .name(member.getName())
                .email(member.getEmail())
                .loginType(member.getLoginType())
                .job(member.getJob())
                .gender(member.getGender())
                .birth(member.getBirth())
                .build();

        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/friends")
    public ApiResponse<MemberResponse.friendListDto> getFriends() {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());

        List<Friend> friends = memberService.getFriends(member);

        return ApiResponse.onSuccess(MemberConverter.toFriendListDto(friends));
    }

    @GetMapping
    public ApiResponse<MemberResponse.memberInfoDto> getMemberInfo() {
        Member member = memberService.findMember(SecurityUtil.getCurrentMemberId());

        MemberResponse.memberInfoDto response = MemberResponse.memberInfoDto.builder()
                .memberId(member.getId())
                .profileImg(member.getProfileImg())
                .name(member.getName())
                .build();

        return ApiResponse.onSuccess(response);
    }

}
