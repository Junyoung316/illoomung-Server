package com.reserve.illoomung.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoreOperatingHours is a Querydsl query type for StoreOperatingHours
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoreOperatingHours extends EntityPathBase<StoreOperatingHours> {

    private static final long serialVersionUID = 983537515L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoreOperatingHours storeOperatingHours = new QStoreOperatingHours("storeOperatingHours");

    public final TimePath<java.time.LocalTime> breakEndTime = createTime("breakEndTime", java.time.LocalTime.class);

    public final TimePath<java.time.LocalTime> breakStartTime = createTime("breakStartTime", java.time.LocalTime.class);

    public final TimePath<java.time.LocalTime> closeTime = createTime("closeTime", java.time.LocalTime.class);

    public final NumberPath<Integer> dayOfWeek = createNumber("dayOfWeek", Integer.class);

    public final NumberPath<Long> hoursId = createNumber("hoursId", Long.class);

    public final BooleanPath isOpen = createBoolean("isOpen");

    public final TimePath<java.time.LocalTime> openTime = createTime("openTime", java.time.LocalTime.class);

    public final QStores store;

    public QStoreOperatingHours(String variable) {
        this(StoreOperatingHours.class, forVariable(variable), INITS);
    }

    public QStoreOperatingHours(Path<? extends StoreOperatingHours> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoreOperatingHours(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoreOperatingHours(PathMetadata metadata, PathInits inits) {
        this(StoreOperatingHours.class, metadata, inits);
    }

    public QStoreOperatingHours(Class<? extends StoreOperatingHours> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.store = inits.isInitialized("store") ? new QStores(forProperty("store"), inits.get("store")) : null;
    }

}

