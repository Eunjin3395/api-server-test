package example.com.member.repository;

import example.com.member.domain.Friend;
import example.com.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findAllByFromMemberAndIsFriend(Member fromMember, Boolean isFriend);
}
