package example.com.member;

import example.com.common.apiPayload.code.status.ErrorStatus;
import example.com.common.apiPayload.exception.handler.MemberHandler;
import example.com.member.domain.Friend;
import example.com.member.domain.LoginType;
import example.com.member.domain.Member;
import example.com.member.dto.MemberRequest;
import example.com.member.repository.FriendRepository;
import example.com.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;

    @Transactional
    public Member join(MemberRequest.signinRequest request, String reqLoginType) {
        LoginType loginType = null;
        if (reqLoginType.equals(LoginType.KAKAO.toString())) {
            loginType = LoginType.KAKAO;
        } else if (reqLoginType.equals(LoginType.APPLE.toString())) {
            loginType = LoginType.APPLE;
        }

        // socialId와 loginType이 일치하는 사용자가 있는지 검증
        boolean isExist = memberRepository.existsBySocialIdAndLoginType(request.getSocialId(),
            loginType);
        if (isExist) {
            throw new MemberHandler(ErrorStatus.MEMBER_EXISTS);
        }

        Member member = MemberConverter.toMember(request, loginType);
        return memberRepository.save(member);

    }

    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    public List<Friend> getFriends(Member member) {
        return friendRepository.findAllByFromMember(member);
    }


}
