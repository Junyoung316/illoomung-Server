package com.reserve.illoomung.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoreOffering is a Querydsl query type for StoreOffering
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoreOffering extends EntityPathBase<StoreOffering> {

    private static final long serialVersionUID = -538321181L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoreOffering storeOffering = new QStoreOffering("storeOffering");

    public final DateTimePath<java.time.Instant> createdAt = createDateTime("createdAt", java.time.Instant.class);

    public final StringPath currency = createString("currency");

    public final StringPath description = createString("description");

    public final StringPath imageUrl = createString("imageUrl");

    public final NumberPath<Long> offeringId = createNumber("offeringId", Long.class);

    public final StringPath offeringName = createString("offeringName");

    public final StringPath price = createString("price");

    public final EnumPath<com.reserve.illoomung.domain.entity.enums.Status> status = createEnum("status", com.reserve.illoomung.domain.entity.enums.Status.class);

    public final QStores store;

    public final DateTimePath<java.time.Instant> updatedAt = createDateTime("updatedAt", java.time.Instant.class);

    public QStoreOffering(String variable) {
        this(StoreOffering.class, forVariable(variable), INITS);
    }

    public QStoreOffering(Path<? extends StoreOffering> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoreOffering(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoreOffering(PathMetadata metadata, PathInits inits) {
        this(StoreOffering.class, metadata, inits);
    }

    public QStoreOffering(Class<? extends StoreOffering> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.store = inits.isInitialized("store") ? new QStores(forProperty("store"), inits.get("store")) : null;
    }

}

