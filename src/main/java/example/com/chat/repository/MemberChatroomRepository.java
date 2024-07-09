package example.com.chat.repository;

import example.com.chat.domain.MemberChatroom;
import example.com.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberChatroomRepository extends JpaRepository<MemberChatroom, Long> {

    @Query("SELECT mc.member FROM MemberChatroom mc WHERE mc.chatroom.id = :chatroomId AND mc.member.id != :memberId")
    Member findTargetMemberByChatroomIdAndMemberId(@Param("chatroomId") Long chatroomId, @Param("memberId") Long memberId);

    Optional<MemberChatroom> findByMemberIdAndChatroomId(Long memberId, Long chatroomId);
}
