package example.com.chat.repository;

import example.com.chat.domain.MemberChatroom;
import java.util.List;

public interface MemberChatroomRepositoryCustom {

    List<MemberChatroom> findActiveMemberChatroomOrderByLastChat(Long memberId);

}
