package com.reserve.illoomung.core.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRefreshTokens is a Querydsl query type for RefreshTokens
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRefreshTokens extends EntityPathBase<RefreshTokens> {

    private static final long serialVersionUID = 324133320L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRefreshTokens refreshTokens = new QRefreshTokens("refreshTokens");

    public final QAccount account;

    public final DateTimePath<java.time.Instant> createdAt = createDateTime("createdAt", java.time.Instant.class);

    public final NumberPath<Long> expiresAt = createNumber("expiresAt", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath revoked = createBoolean("revoked");

    public final StringPath token = createString("token");

    public QRefreshTokens(String variable) {
        this(RefreshTokens.class, forVariable(variable), INITS);
    }

    public QRefreshTokens(Path<? extends RefreshTokens> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRefreshTokens(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRefreshTokens(PathMetadata metadata, PathInits inits) {
        this(RefreshTokens.class, metadata, inits);
    }

    public QRefreshTokens(Class<? extends RefreshTokens> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new QAccount(forProperty("account")) : null;
    }

}

