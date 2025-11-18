package com.reserve.illoomung.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoreApprovalRequests is a Querydsl query type for StoreApprovalRequests
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoreApprovalRequests extends EntityPathBase<StoreApprovalRequests> {

    private static final long serialVersionUID = -1236215548L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoreApprovalRequests storeApprovalRequests = new QStoreApprovalRequests("storeApprovalRequests");

    public final com.reserve.illoomung.core.auditing.QBaseTimeEntity _super = new com.reserve.illoomung.core.auditing.QBaseTimeEntity(this);

    public final com.reserve.illoomung.core.domain.entity.QAccount account;

    public final com.reserve.illoomung.core.domain.entity.QAccount admin;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath rejectionReason = createString("rejectionReason");

    public final NumberPath<Long> requestId = createNumber("requestId", Long.class);

    public final EnumPath<com.reserve.illoomung.core.domain.entity.enums.RoleStatus> status = createEnum("status", com.reserve.illoomung.core.domain.entity.enums.RoleStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QStoreApprovalRequests(String variable) {
        this(StoreApprovalRequests.class, forVariable(variable), INITS);
    }

    public QStoreApprovalRequests(Path<? extends StoreApprovalRequests> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoreApprovalRequests(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoreApprovalRequests(PathMetadata metadata, PathInits inits) {
        this(StoreApprovalRequests.class, metadata, inits);
    }

    public QStoreApprovalRequests(Class<? extends StoreApprovalRequests> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new com.reserve.illoomung.core.domain.entity.QAccount(forProperty("account")) : null;
        this.admin = inits.isInitialized("admin") ? new com.reserve.illoomung.core.domain.entity.QAccount(forProperty("admin")) : null;
    }

}

