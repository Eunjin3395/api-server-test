package example.com.member.repository;

import example.com.member.domain.Friend;
import example.com.member.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Friend> findAllByFromMember(Member fromMember);
}
