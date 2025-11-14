package com.reserve.illoomung.core.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLoginAttempts is a Querydsl query type for LoginAttempts
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLoginAttempts extends EntityPathBase<LoginAttempts> {

    private static final long serialVersionUID = 1690767170L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLoginAttempts loginAttempts = new QLoginAttempts("loginAttempts");

    public final QAccount account;

    public final DateTimePath<java.time.Instant> attemptedAt = createDateTime("attemptedAt", java.time.Instant.class);

    public final StringPath failReason = createString("failReason");

    public final StringPath ipAddress = createString("ipAddress");

    public final NumberPath<Long> loginId = createNumber("loginId", Long.class);

    public final BooleanPath success = createBoolean("success");

    public final StringPath userAgent = createString("userAgent");

    public QLoginAttempts(String variable) {
        this(LoginAttempts.class, forVariable(variable), INITS);
    }

    public QLoginAttempts(Path<? extends LoginAttempts> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLoginAttempts(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLoginAttempts(PathMetadata metadata, PathInits inits) {
        this(LoginAttempts.class, metadata, inits);
    }

    public QLoginAttempts(Class<? extends LoginAttempts> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new QAccount(forProperty("account")) : null;
    }

}

