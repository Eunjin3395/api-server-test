package example.com.chat.repository;

import example.com.chat.domain.MemberChatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberChatroomRepository extends JpaRepository<MemberChatroom, Long> {
}
