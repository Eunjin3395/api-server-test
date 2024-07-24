package example.com.chat.repository;

import static example.com.chat.domain.QChat.chat;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import example.com.chat.domain.Chat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@Slf4j
@RequiredArgsConstructor
public class ChatRepositoryCustomImpl implements ChatRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Chat> findChatsByCursor(String cursor, Long chatroomId, Pageable pageable) {

        // cursor string decode
        LocalDateTime cursorDate = TimeCursorParser.decode(cursor);

        List<Chat> result = queryFactory.selectFrom(chat)
            .where(chat.chatroom.id.eq(chatroomId), createdBefore(cursorDate))
            .orderBy(chat.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize() + 1) // 다음 페이지가 있는지 확인하기 위해 +1
            .fetch();

        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            result.remove(pageable.getPageSize());
            hasNext = true;
        }

        // createdAt 오름차순으로 정렬
        Collections.reverse(result);

        return new SliceImpl<>(result, pageable, hasNext);
    }

    private BooleanExpression createdBefore(LocalDateTime cursorDate) {
        if (cursorDate != null) {
            return chat.createdAt.lt(cursorDate);
        }

        return null;
    }
}
