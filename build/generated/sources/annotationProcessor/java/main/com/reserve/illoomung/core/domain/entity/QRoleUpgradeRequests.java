package com.reserve.illoomung.core.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoleUpgradeRequests is a Querydsl query type for RoleUpgradeRequests
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoleUpgradeRequests extends EntityPathBase<RoleUpgradeRequests> {

    private static final long serialVersionUID = -964720163L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoleUpgradeRequests roleUpgradeRequests = new QRoleUpgradeRequests("roleUpgradeRequests");

    public final QAccount account;

    public final QAccount admin;

    public final DateTimePath<java.time.Instant> createdAt = createDateTime("createdAt", java.time.Instant.class);

    public final DateTimePath<java.time.Instant> processedAt = createDateTime("processedAt", java.time.Instant.class);

    public final StringPath rejectionReason = createString("rejectionReason");

    public final NumberPath<Long> requestId = createNumber("requestId", Long.class);

    public final EnumPath<com.reserve.illoomung.core.domain.entity.enums.RoleStatus> status = createEnum("status", com.reserve.illoomung.core.domain.entity.enums.RoleStatus.class);

    public QRoleUpgradeRequests(String variable) {
        this(RoleUpgradeRequests.class, forVariable(variable), INITS);
    }

    public QRoleUpgradeRequests(Path<? extends RoleUpgradeRequests> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoleUpgradeRequests(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoleUpgradeRequests(PathMetadata metadata, PathInits inits) {
        this(RoleUpgradeRequests.class, metadata, inits);
    }

    public QRoleUpgradeRequests(Class<? extends RoleUpgradeRequests> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new QAccount(forProperty("account")) : null;
        this.admin = inits.isInitialized("admin") ? new QAccount(forProperty("admin")) : null;
    }

}

