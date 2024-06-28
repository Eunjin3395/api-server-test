package example.com.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import example.com.member.domain.LoginType;
import example.com.member.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialIdAndLoginType(Long socialId, LoginType loginType);

    boolean existsBySocialIdAndLoginType(Long socialId, LoginType loginType);


    Optional<Member> findByIdAndSocialId(Long memberId, Long socialId);
}
