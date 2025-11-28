package com.reserve.illoomung.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOfferingCategoryMapping is a Querydsl query type for OfferingCategoryMapping
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOfferingCategoryMapping extends EntityPathBase<OfferingCategoryMapping> {

    private static final long serialVersionUID = -309799706L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOfferingCategoryMapping offeringCategoryMapping = new QOfferingCategoryMapping("offeringCategoryMapping");

    public final QOfferingCategory category;

    public final QStoreOffering offering;

    public QOfferingCategoryMapping(String variable) {
        this(OfferingCategoryMapping.class, forVariable(variable), INITS);
    }

    public QOfferingCategoryMapping(Path<? extends OfferingCategoryMapping> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOfferingCategoryMapping(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOfferingCategoryMapping(PathMetadata metadata, PathInits inits) {
        this(OfferingCategoryMapping.class, metadata, inits);
    }

    public QOfferingCategoryMapping(Class<? extends OfferingCategoryMapping> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QOfferingCategory(forProperty("category")) : null;
        this.offering = inits.isInitialized("offering") ? new QStoreOffering(forProperty("offering"), inits.get("offering")) : null;
    }

}

