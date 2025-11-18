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

    public final com.reserve.illoomung.core.auditing.QBaseTimeEntity _super = new com.reserve.illoomung.core.auditing.QBaseTimeEntity(this);

    public final NumberPath<Long> categoryId = createNumber("categoryId", Long.class);

    public final StringPath categoryName = createString("categoryName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    public final EnumPath<com.reserve.illoomung.domain.entity.enums.Status> status = createEnum("status", com.reserve.illoomung.domain.entity.enums.Status.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

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

