package example.com.chat.repository;

import static example.com.chat.domain.QChat.chat;
import static example.com.chat.domain.QMemberChatroom.memberChatroom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import example.com.chat.domain.Chat;
import java.util.ArrayList;
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

        if (unreadChats.isEmpty()) { // 안읽은 메시지가 없는 경우, 읽은 메시지 20개 조회해 리턴, 여기서는 cursor가 필요 없음

            List<Chat> readChats = queryFactory.selectFrom(chat)
                .where(
                    chat.chatroom.id.eq(chatroomId)
                )
                .orderBy(chat.createdAt.desc())
                .limit(pageable.getPageSize() + 1) // 다음 페이지가 있는지 확인하기 위해 +1
                .fetch();

            boolean hasNext = false;
            if (readChats.size() > pageable.getPageSize()) {
                readChats.remove(pageable.getPageSize());
                hasNext = true;
            }

            // createdAt 오름차순으로 정렬
            Collections.reverse(readChats);

            return new SliceImpl<>(readChats, pageable, hasNext);

        } else if (unreadChats.size() >= 20) { // 안읽은 메시지 개수가 20개 이상인 경우, 안읽은 메시지만 모두 리턴
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

        } else { // 안읽은 메시지가 1~19개인 경우, 읽은 메시지를 추가로 조회해 20개 개수 맞춰 리턴
            Pageable newPageable = PageRequest.of(0, (PAGE_SIZE - unreadChats.size()));
            // 가장 오래된 unreadChat의 timestamp를 기준으로 추가 메시지 조회
            Chat oldestUnreadChat = unreadChats.get(unreadChats.size() - 1);
            List<Chat> readChats = queryFactory.selectFrom(chat)
                .where(
                    chat.chatroom.id.eq(chatroomId),
                    createdBefore(oldestUnreadChat.getTimestamp())
                )
                .orderBy(chat.createdAt.desc())
                .limit(newPageable.getPageSize() + 1) // 다음 페이지가 있는지 확인하기 위해 +1
                .fetch();

            boolean hasNext = false;
            if (readChats.size() > newPageable.getPageSize()) {
                readChats.remove(newPageable.getPageSize());
                hasNext = true;
            }

            // unreadChats 뒤에 readChats를 이어 붙임
            List<Chat> combinedChats = new ArrayList<>(unreadChats);
            combinedChats.addAll(readChats);

            // ceatedAt 오름차순으로 정렬
            Collections.reverse(combinedChats);

            return new SliceImpl<>(combinedChats, pageable, hasNext);

        }
    }

    //--- BooleanExpression ---//
    private BooleanExpression createdBefore(Long cursorTimestamp) {
        return cursorTimestamp != null ? chat.timestamp.lt(cursorTimestamp) : null;
    }

    private BooleanExpression createdAtGreaterThanSubQuery(Long memberChatroomId) {
        return chat.createdAt.gt(
            JPAExpressions.select(memberChatroom.lastViewDateTime)
                .from(memberChatroom)
                .where(memberChatroom.id.eq(memberChatroomId))
        );
    }
}
