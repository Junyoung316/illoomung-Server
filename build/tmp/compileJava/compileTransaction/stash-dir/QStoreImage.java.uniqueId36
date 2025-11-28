package com.reserve.illoomung.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoreImage is a Querydsl query type for StoreImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoreImage extends EntityPathBase<StoreImage> {

    private static final long serialVersionUID = 1141804958L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoreImage storeImage = new QStoreImage("storeImage");

    public final com.reserve.illoomung.core.auditing.QBaseTimeEntity _super = new com.reserve.illoomung.core.auditing.QBaseTimeEntity(this);

    public final StringPath altText = createString("altText");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> imageId = createNumber("imageId", Long.class);

    public final EnumPath<com.reserve.illoomung.domain.entity.enums.ImageType> imageType = createEnum("imageType", com.reserve.illoomung.domain.entity.enums.ImageType.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    public final QStores store;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QStoreImage(String variable) {
        this(StoreImage.class, forVariable(variable), INITS);
    }

    public QStoreImage(Path<? extends StoreImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoreImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoreImage(PathMetadata metadata, PathInits inits) {
        this(StoreImage.class, metadata, inits);
    }

    public QStoreImage(Class<? extends StoreImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.store = inits.isInitialized("store") ? new QStores(forProperty("store"), inits.get("store")) : null;
    }

}

