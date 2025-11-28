package com.reserve.illoomung.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoreAmenityMapping is a Querydsl query type for StoreAmenityMapping
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoreAmenityMapping extends EntityPathBase<StoreAmenityMapping> {

    private static final long serialVersionUID = 134890738L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoreAmenityMapping storeAmenityMapping = new QStoreAmenityMapping("storeAmenityMapping");

    public final QAmenity amenity;

    public final QStores store;

    public QStoreAmenityMapping(String variable) {
        this(StoreAmenityMapping.class, forVariable(variable), INITS);
    }

    public QStoreAmenityMapping(Path<? extends StoreAmenityMapping> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoreAmenityMapping(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoreAmenityMapping(PathMetadata metadata, PathInits inits) {
        this(StoreAmenityMapping.class, metadata, inits);
    }

    public QStoreAmenityMapping(Class<? extends StoreAmenityMapping> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.amenity = inits.isInitialized("amenity") ? new QAmenity(forProperty("amenity")) : null;
        this.store = inits.isInitialized("store") ? new QStores(forProperty("store"), inits.get("store")) : null;
    }

}

