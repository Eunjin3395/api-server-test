package example.com.chat.repository;

import example.com.chat.domain.MemberChatroom;
import example.com.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberChatroomRepository extends JpaRepository<MemberChatroom, Long> {

    @Query("SELECT mc.member FROM MemberChatroom mc WHERE mc.chatroom.id = :chatroomId AND mc.member.id != :memberId")
    Member findMemberByChatroomIdAndMemberIdNot(@Param("chatroomId") Long chatroomId, @Param("memberId") Long memberId);

}
