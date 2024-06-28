package example.com.member.domain;

import jakarta.persistence.*;
import lombok.*;
import example.com.common.domain.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseDateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private Long socialId;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 30)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)", nullable = false)
    private Gender gender;

    @Column(nullable = false, length = 50)
    private String job;

    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private MemberStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    private RoleType roleType;
}
