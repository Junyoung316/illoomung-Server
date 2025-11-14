package com.reserve.illoomung.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOfferingCategory is a Querydsl query type for OfferingCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOfferingCategory extends EntityPathBase<OfferingCategory> {

    private static final long serialVersionUID = 267627208L;

    public static final QOfferingCategory offeringCategory = new QOfferingCategory("offeringCategory");

    public final NumberPath<Long> categoryId = createNumber("categoryId", Long.class);

    public final StringPath categoryName = createString("categoryName");

    public final DateTimePath<java.time.Instant> createdAt = createDateTime("createdAt", java.time.Instant.class);

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    public final EnumPath<com.reserve.illoomung.domain.entity.enums.Status> status = createEnum("status", com.reserve.illoomung.domain.entity.enums.Status.class);

    public final DateTimePath<java.time.Instant> updatedAt = createDateTime("updatedAt", java.time.Instant.class);

    public QOfferingCategory(String variable) {
        super(OfferingCategory.class, forVariable(variable));
    }

    public QOfferingCategory(Path<? extends OfferingCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOfferingCategory(PathMetadata metadata) {
        super(OfferingCategory.class, metadata);
    }

}

