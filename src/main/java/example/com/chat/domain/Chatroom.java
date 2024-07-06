package example.com.chat.domain;

import example.com.common.domain.BaseDateTimeEntity;
import example.com.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Chatroom extends BaseDateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(30)", nullable = false)
    private ChatroomType chatroomType;

    @Column(columnDefinition = "TEXT")
    private String postUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_member_id")
    private Member startMember;
}
