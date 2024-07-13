package example.com.chat.repository;

import example.com.chat.domain.Chat;
import example.com.chat.domain.Chatroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Chat findFirstByChatroomIdOrderByCreatedAtDesc(Long chatroomId);

    @Query("SELECT COUNT(c) FROM Chat c WHERE c.chatroom.id = :chatroomId AND c.fromMember.id != :fromMemberId AND c.createdAt > :lastViewDateTime")
    Integer countChatsByChatroomIdAndFromMemberIdAfterLastViewDateTime(
            @Param("chatroomId") Long chatroomId,
            @Param("fromMemberId") Long fromMemberId,
            @Param("lastViewDateTime") LocalDateTime lastViewDateTime);

    Page<Chat> findAllByChatroom(Chatroom chatroom, Pageable pageable);

}
