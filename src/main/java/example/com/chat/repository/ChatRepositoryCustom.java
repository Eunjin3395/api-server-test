package example.com.chat.repository;

import example.com.chat.domain.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatRepositoryCustom {

    Slice<Chat> findChatsByCursor(String cursor, Long chatroomId, Pageable pageable);
}
