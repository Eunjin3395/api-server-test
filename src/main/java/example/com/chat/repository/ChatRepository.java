package example.com.chat.repository;

import example.com.chat.domain.Chat;
import example.com.chat.domain.Chatroom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {

    Chat findFirstByChatroomIdOrderByCreatedAtDesc(Long chatroomId);

    Optional<Chat> findByChatroomAndTimestamp(Chatroom chatroom, Long timestamp);
}
