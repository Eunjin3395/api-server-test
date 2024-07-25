package example.com.chat.repository;

import static example.com.chat.domain.QChat.chat;
import static example.com.chat.domain.QChatroom.chatroom;
import static example.com.chat.domain.QMemberChatroom.memberChatroom;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import example.com.chat.domain.MemberChatroom;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MemberChatroomRepositoryCustomImpl implements MemberChatroomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberChatroom> findActiveMemberChatroomOrderByLastChat(Long memberId) {
        return queryFactory.selectFrom(memberChatroom)
            .join(memberChatroom.chatroom, chatroom)
            .where(
                memberChatroom.member.id.eq(memberId),
                memberChatroom.lastJoinDate.isNotNull()
            )
            .orderBy(new OrderSpecifier<>(Order.DESC,
                    JPAExpressions.select(chat.createdAt.max())
                        .from(chat)
                        .where(chat.chatroom.eq(chatroom))
                )
            )
            .fetch();
    }
}
