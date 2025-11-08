package com.reserve.illoomung.core.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAccount is a Querydsl query type for Account
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAccount extends EntityPathBase<Account> {

    private static final long serialVersionUID = 212732384L;

    public static final QAccount account = new QAccount("account");

    public final NumberPath<Long> accountId = createNumber("accountId", Long.class);

    public final DateTimePath<java.time.Instant> createdAt = createDateTime("createdAt", java.time.Instant.class);

    public final DateTimePath<java.time.Instant> dormantAt = createDateTime("dormantAt", java.time.Instant.class);

    public final StringPath email = createString("email");

    public final StringPath emailHash = createString("emailHash");

    public final DateTimePath<java.time.Instant> lastLoginAt = createDateTime("lastLoginAt", java.time.Instant.class);

    public final StringPath passwordHash = createString("passwordHash");

    public final EnumPath<com.reserve.illoomung.core.domain.entity.enums.Role> role = createEnum("role", com.reserve.illoomung.core.domain.entity.enums.Role.class);

    public final StringPath socialId = createString("socialId");

    public final StringPath socialIdHash = createString("socialIdHash");

    public final EnumPath<com.reserve.illoomung.core.domain.entity.enums.SocialProvider> socialProvider = createEnum("socialProvider", com.reserve.illoomung.core.domain.entity.enums.SocialProvider.class);

    public final EnumPath<com.reserve.illoomung.core.domain.entity.enums.Status> status = createEnum("status", com.reserve.illoomung.core.domain.entity.enums.Status.class);

    public final DateTimePath<java.time.Instant> updatedAt = createDateTime("updatedAt", java.time.Instant.class);

    public final DateTimePath<java.time.Instant> withdrawnAt = createDateTime("withdrawnAt", java.time.Instant.class);

    public QAccount(String variable) {
        super(Account.class, forVariable(variable));
    }

    public QAccount(Path<? extends Account> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAccount(PathMetadata metadata) {
        super(Account.class, metadata);
    }

}

