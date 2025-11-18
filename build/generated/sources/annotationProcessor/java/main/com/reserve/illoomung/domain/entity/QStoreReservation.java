package com.reserve.illoomung.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoreReservation is a Querydsl query type for StoreReservation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoreReservation extends EntityPathBase<StoreReservation> {

    private static final long serialVersionUID = 2015767503L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoreReservation storeReservation = new QStoreReservation("storeReservation");

    public final com.reserve.illoomung.core.auditing.QBaseTimeEntity _super = new com.reserve.illoomung.core.auditing.QBaseTimeEntity(this);

    public final com.reserve.illoomung.core.domain.entity.QAccount account;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QStoreOffering offering;

    public final StringPath requestNote = createString("requestNote");

    public final DateTimePath<java.time.LocalDateTime> reservationDatetime = createDateTime("reservationDatetime", java.time.LocalDateTime.class);

    public final NumberPath<Long> reservationId = createNumber("reservationId", Long.class);

    public final EnumPath<com.reserve.illoomung.domain.entity.enums.ReservationStatus> status = createEnum("status", com.reserve.illoomung.domain.entity.enums.ReservationStatus.class);

    public final QStores store;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QStoreReservation(String variable) {
        this(StoreReservation.class, forVariable(variable), INITS);
    }

    public QStoreReservation(Path<? extends StoreReservation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoreReservation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoreReservation(PathMetadata metadata, PathInits inits) {
        this(StoreReservation.class, metadata, inits);
    }

    public QStoreReservation(Class<? extends StoreReservation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new com.reserve.illoomung.core.domain.entity.QAccount(forProperty("account")) : null;
        this.offering = inits.isInitialized("offering") ? new QStoreOffering(forProperty("offering"), inits.get("offering")) : null;
        this.store = inits.isInitialized("store") ? new QStores(forProperty("store"), inits.get("store")) : null;
    }

}

