package example.com.chat.repository;

import example.com.chat.domain.Chat;
import example.com.chat.domain.Chatroom;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {

    Chat findFirstByChatroomIdOrderByCreatedAtDesc(Long chatroomId);

    @Query("SELECT COUNT(c) FROM Chat c WHERE c.chatroom.id = :chatroomId AND c.fromMember.id != :fromMemberId AND c.createdAt > :lastViewDate")
    Integer countChatsByChatroomIdAndFromMemberIdAfterLastViewDate(
        @Param("chatroomId") Long chatroomId,
        @Param("fromMemberId") Long fromMemberId,
        @Param("lastViewDate") LocalDateTime lastViewDate);

    Optional<Chat> findByChatroomAndTimestamp(Chatroom chatroom, Long timestamp);
}
