package example.com.chat.repository;

import example.com.chat.domain.Chat;
import example.com.chat.domain.Chatroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Chat findFirstByChatroomIdOrderByCreatedAtDesc(Long chatroomId);

    @Query("SELECT COUNT(c) FROM Chat c WHERE c.chatroom.id = :chatroomId AND c.fromMember.id != :fromMemberId AND c.isRead = false")
    Integer countUnreadChatsByChatroomIdAndFromMemberId(@Param("chatroomId") Long chatroomId, @Param("fromMemberId") Long fromMemberId);

    Page<Chat> findAllByChatroom(Chatroom chatroom, Pageable pageable);

    @Query("SELECT c FROM Chat c WHERE c.chatroom.id = :chatroomId AND c.fromMember.id != :fromMemberId AND c.isRead = false")
    List<Chat> findUnreadChatsByChatroomIdAndFromMemberId(@Param("chatroomId") Long chatroomId, @Param("fromMemberId") Long fromMemberId);

}
