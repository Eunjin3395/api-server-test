package example.com.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFriendRequests is a Querydsl query type for FriendRequests
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFriendRequests extends EntityPathBase<FriendRequests> {

    private static final long serialVersionUID = -1427228663L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFriendRequests friendRequests = new QFriendRequests("friendRequests");

    public final example.com.common.domain.QBaseDateTimeEntity _super = new example.com.common.domain.QBaseDateTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QMember fromMember;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isApproved = createBoolean("isApproved");

    public final QMember toMember;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QFriendRequests(String variable) {
        this(FriendRequests.class, forVariable(variable), INITS);
    }

    public QFriendRequests(Path<? extends FriendRequests> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFriendRequests(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFriendRequests(PathMetadata metadata, PathInits inits) {
        this(FriendRequests.class, metadata, inits);
    }

    public QFriendRequests(Class<? extends FriendRequests> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fromMember = inits.isInitialized("fromMember") ? new QMember(forProperty("fromMember")) : null;
        this.toMember = inits.isInitialized("toMember") ? new QMember(forProperty("toMember")) : null;
    }

}

