package example.com.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import example.com.common.apiPayload.code.status.ErrorStatus;
import example.com.common.apiPayload.exception.JwtAuthenticationException;
import example.com.member.domain.Member;
import example.com.member.domain.MemberStatus;
import example.com.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public UserDetails loadUserByMemberIdAndSocialId(Long memberId, Long socialId) throws UsernameNotFoundException {
        Member member = memberRepository.findByIdAndSocialId(memberId, socialId)
                .orElseThrow(() -> new JwtAuthenticationException(ErrorStatus.MEMBER_NOT_FOUND));
        if (member.getStatus().equals(MemberStatus.INACTIVE)) { // 탈퇴한 회원인 경우 에러 발생
            throw new JwtAuthenticationException(ErrorStatus.INACTIVE_MEMBER);
        }
        return new CustomUserDetails(member.getId(), member.getSocialId(), member.getLoginType(), member.getRoleType());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("loadUserByUsername(String username) is not supported. Use loadUserByMemberIdAndSocialId(Long socialId, LoginType loginType) instead.");
    }
}
