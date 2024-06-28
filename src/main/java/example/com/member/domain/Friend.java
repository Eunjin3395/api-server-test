package example.com.member.domain;

import example.com.common.domain.BaseDateTimeEntity;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Friend extends BaseDateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "friend_id")
    private Long id;

    @Column(nullable = false)
    private Boolean isFriend;

    @Column(nullable = false)
    private Boolean isLiked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id", nullable = false)
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id", nullable = false)
    private Member toMember;
}
