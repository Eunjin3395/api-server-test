package example.com.chat.repository;

import static example.com.chat.domain.QChat.chat;
import static example.com.chat.domain.QMemberChatroom.memberChatroom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import example.com.chat.domain.Chat;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@Slf4j
@RequiredArgsConstructor
public class ChatRepositoryCustomImpl implements ChatRepositoryCustom {

    private final static int PAGE_SIZE = 20;

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Chat> findChatsByCursor(Long cursor, Long chatroomId, Pageable pageable) {

        List<Chat> result = queryFactory.selectFrom(chat)
            .where(
                chat.chatroom.id.eq(chatroomId),
                createdBefore(cursor)
            )
            .orderBy(chat.createdAt.desc())
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

    @Override
    public Slice<Chat> findRecentChats(Long chatroomId, Long memberChatroomId) {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

        // 안읽은 메시지 모두 조회
        List<Chat> unreadChats = queryFactory.selectFrom(chat)
            .where(
                chat.chatroom.id.eq(chatroomId),
                createdAtGreaterThanSubQuery(memberChatroomId)
            )
            .orderBy(chat.createdAt.desc())
            .fetch();

        if (unreadChats.size() >= 20) { // 안읽은 메시지 개수가 20개 이상인 경우, 안읽은 메시지만 모두 리턴

            // 가장 오래된 unreadChat의 timestamp를 기준으로 추가 메시지 조회 (hasNext 여부 판단을 위함)
            Chat oldestUnreadChat = unreadChats.get(unreadChats.size() - 1);
            List<Chat> additionalChats = queryFactory.selectFrom(chat)
                .where(
                    chat.chatroom.id.eq(oldestUnreadChat.getChatroom().getId()),
                    createdBefore(oldestUnreadChat.getTimestamp())
                )
                .orderBy(chat.createdAt.desc())
                .limit(1) // 하나의 추가 메시지만 확인하면 충분
                .fetch();

            boolean hasNext = !additionalChats.isEmpty();

            // createdAt 오름차순으로 정렬
            Collections.reverse(unreadChats);

            return new SliceImpl<>(unreadChats, Pageable.unpaged(), hasNext);
        } else { // 안읽은 메시지가 20개 미만인 경우, 최근 메시지 20개를 조회해 리턴
            List<Chat> chats = queryFactory.selectFrom(chat)
                .where(
                    chat.chatroom.id.eq(chatroomId)
                )
                .orderBy(chat.createdAt.desc())
                .limit(pageable.getPageSize() + 1) // 다음 페이지가 있는지 확인하기 위해 +1
                .fetch();

            boolean hasNext = false;
            if (chats.size() > pageable.getPageSize()) {
                chats.remove(pageable.getPageSize());
                hasNext = true;
            }

            // createdAt 오름차순으로 정렬
            Collections.reverse(chats);

            return new SliceImpl<>(chats, pageable, hasNext);
        }

    }

    @Override
    public Integer countUnreadChats(Long chatroomId, Long memberChatroomId) {

        Long countResult = queryFactory.select(chat.count())
            .from(chat)
            .where(
                chat.chatroom.id.eq(chatroomId),
                createdAtGreaterThanSubQuery(memberChatroomId)
            )
            .fetchOne();

        return countResult != null ? countResult.intValue() : null;
    }

    //--- BooleanExpression ---//
    private BooleanExpression createdBefore(Long cursorTimestamp) {
        return cursorTimestamp != null ? chat.timestamp.lt(cursorTimestamp) : null;
    }

    private BooleanExpression createdAtGreaterThanSubQuery(Long memberChatroomId) {
        return chat.createdAt.gt(
            JPAExpressions.select(memberChatroom.lastViewDate)
                .from(memberChatroom)
                .where(memberChatroom.id.eq(memberChatroomId))
        );
    }
}
