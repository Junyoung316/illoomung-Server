package com.reserve.illoomung.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAmenity is a Querydsl query type for Amenity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAmenity extends EntityPathBase<Amenity> {

    private static final long serialVersionUID = 2081408661L;

    public static final QAmenity amenity = new QAmenity("amenity");

    public final com.reserve.illoomung.core.auditing.QBaseTimeEntity _super = new com.reserve.illoomung.core.auditing.QBaseTimeEntity(this);

    public final NumberPath<Long> amenityId = createNumber("amenityId", Long.class);

    public final StringPath amenityName = createString("amenityName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final StringPath iconUrl = createString("iconUrl");

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    public final EnumPath<com.reserve.illoomung.domain.entity.enums.Status> status = createEnum("status", com.reserve.illoomung.domain.entity.enums.Status.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAmenity(String variable) {
        super(Amenity.class, forVariable(variable));
    }

    public QAmenity(Path<? extends Amenity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAmenity(PathMetadata metadata) {
        super(Amenity.class, metadata);
    }

}

