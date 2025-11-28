package com.reserve.illoomung.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStores is a Querydsl query type for Stores
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStores extends EntityPathBase<Stores> {

    private static final long serialVersionUID = 1697611862L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStores stores = new QStores("stores");

    public final com.reserve.illoomung.core.auditing.QBaseTimeEntity _super = new com.reserve.illoomung.core.auditing.QBaseTimeEntity(this);

    public final StringPath addrDepth1 = createString("addrDepth1");

    public final StringPath addrDepth2 = createString("addrDepth2");

    public final StringPath addrDepth3 = createString("addrDepth3");

    public final StringPath address = createString("address");

    public final StringPath addressDetails = createString("addressDetails");

    public final StringPath addressDetailsHash = createString("addressDetailsHash");

    public final StringPath addressFullHash = createString("addressFullHash");

    public final ListPath<StoreAmenityMapping, QStoreAmenityMapping> amenityMappings = this.<StoreAmenityMapping, QStoreAmenityMapping>createList("amenityMappings", StoreAmenityMapping.class, QStoreAmenityMapping.class, PathInits.DIRECT2);

    public final StringPath bcode = createString("bcode");

    public final ListPath<StoreCategoryMapping, QStoreCategoryMapping> categoryMappings = this.<StoreCategoryMapping, QStoreCategoryMapping>createList("categoryMappings", StoreCategoryMapping.class, QStoreCategoryMapping.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final ListPath<StoreImage, QStoreImage> images = this.<StoreImage, QStoreImage>createList("images", StoreImage.class, QStoreImage.class, PathInits.DIRECT2);

    public final StringPath instagramUrl = createString("instagramUrl");

    public final ListPath<StoreOffering, QStoreOffering> offerings = this.<StoreOffering, QStoreOffering>createList("offerings", StoreOffering.class, QStoreOffering.class, PathInits.DIRECT2);

    public final ListPath<StoreOperatingHours, QStoreOperatingHours> operatingHours = this.<StoreOperatingHours, QStoreOperatingHours>createList("operatingHours", StoreOperatingHours.class, QStoreOperatingHours.class, PathInits.DIRECT2);

    public final com.reserve.illoomung.core.domain.entity.QAccount owner;

    public final StringPath phone = createString("phone");

    public final EnumPath<com.reserve.illoomung.domain.entity.enums.StoreStatus> status = createEnum("status", com.reserve.illoomung.domain.entity.enums.StoreStatus.class);

    public final NumberPath<Long> storeId = createNumber("storeId", Long.class);

    public final StringPath storeName = createString("storeName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath websiteUrl = createString("websiteUrl");

    public QStores(String variable) {
        this(Stores.class, forVariable(variable), INITS);
    }

    public QStores(Path<? extends Stores> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStores(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStores(PathMetadata metadata, PathInits inits) {
        this(Stores.class, metadata, inits);
    }

    public QStores(Class<? extends Stores> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.owner = inits.isInitialized("owner") ? new com.reserve.illoomung.core.domain.entity.QAccount(forProperty("owner")) : null;
    }

}

