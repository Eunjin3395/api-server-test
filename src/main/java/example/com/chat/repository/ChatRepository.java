package example.com.chat.repository;

import example.com.chat.domain.Chat;
import example.com.chat.domain.Chatroom;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {

    Chat findFirstByChatroomIdOrderByCreatedAtDesc(Long chatroomId);

    @Query("SELECT COUNT(c) FROM Chat c WHERE c.chatroom.id = :chatroomId AND c.fromMember.id != :fromMemberId AND c.createdAt > :lastViewDateTime")
    Integer countChatsByChatroomIdAndFromMemberIdAfterLastViewDateTime(
        @Param("chatroomId") Long chatroomId,
        @Param("fromMemberId") Long fromMemberId,
        @Param("lastViewDateTime") LocalDateTime lastViewDateTime);

    Page<Chat> findAllByChatroom(Chatroom chatroom, Pageable pageable);


    @Query("SELECT c FROM Chat c WHERE c.chatroom.id = :chatroomId ORDER BY c.createdAt DESC")
    List<Chat> findTop20ByChatroomIdOrderByCreatedAtDesc(
        @Param("chatroomId") Long chatroomId,
        Pageable pageable);

    @Query("SELECT c FROM Chat c " +
        "WHERE c.chatroom.id = (SELECT mc.chatroom.id FROM MemberChatroom mc WHERE mc.id = :memberChatroomId) "
        +
        "AND c.createdAt > (SELECT mc.lastViewDateTime FROM MemberChatroom mc WHERE mc.id = :memberChatroomId)")
    List<Chat> findUnreadChats(@Param("memberChatroomId") Long memberChatroomId);
}
