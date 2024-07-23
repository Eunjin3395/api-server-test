package example.com.chat.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatroom is a Querydsl query type for Chatroom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatroom extends EntityPathBase<Chatroom> {

    private static final long serialVersionUID = -911499784L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatroom chatroom = new QChatroom("chatroom");

    public final example.com.common.domain.QBaseDateTimeEntity _super = new example.com.common.domain.QBaseDateTimeEntity(this);

    public final EnumPath<ChatroomType> chatroomType = createEnum("chatroomType", ChatroomType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath postUrl = createString("postUrl");

    public final example.com.member.domain.QMember startMember;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath uuid = createString("uuid");

    public QChatroom(String variable) {
        this(Chatroom.class, forVariable(variable), INITS);
    }

    public QChatroom(Path<? extends Chatroom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatroom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatroom(PathMetadata metadata, PathInits inits) {
        this(Chatroom.class, metadata, inits);
    }

    public QChatroom(Class<? extends Chatroom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.startMember = inits.isInitialized("startMember") ? new example.com.member.domain.QMember(forProperty("startMember")) : null;
    }

}

