package example.com.chat.repository;

import example.com.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Chat findFirstByChatroomIdOrderByCreatedAtDesc(Long chatroomId);

    @Query("SELECT COUNT(c) FROM Chat c WHERE c.chatroom.id = :chatroomId AND c.fromMember.id != :fromMemberId AND c.isRead = false")
    Integer countUnreadChatsByChatroomIdAndFromMemberId(@Param("chatroomId") Long chatroomId, @Param("fromMemberId") Long fromMemberId);


}
