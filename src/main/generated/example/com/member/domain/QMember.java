package example.com.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1935010081L;

    public static final QMember member = new QMember("member1");

    public final example.com.common.domain.QBaseDateTimeEntity _super = new example.com.common.domain.QBaseDateTimeEntity(this);

    public final DatePath<java.time.LocalDate> birth = createDate("birth", java.time.LocalDate.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final EnumPath<Gender> gender = createEnum("gender", Gender.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath job = createString("job");

    public final EnumPath<LoginType> loginType = createEnum("loginType", LoginType.class);

    public final ListPath<example.com.chat.domain.MemberChatroom, example.com.chat.domain.QMemberChatroom> memberChatroomList = this.<example.com.chat.domain.MemberChatroom, example.com.chat.domain.QMemberChatroom>createList("memberChatroomList", example.com.chat.domain.MemberChatroom.class, example.com.chat.domain.QMemberChatroom.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath profileImg = createString("profileImg");

    public final EnumPath<RoleType> roleType = createEnum("roleType", RoleType.class);

    public final NumberPath<Long> socialId = createNumber("socialId", Long.class);

    public final EnumPath<MemberStatus> status = createEnum("status", MemberStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

