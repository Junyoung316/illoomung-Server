package com.reserve.illoomung.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoreCategory is a Querydsl query type for StoreCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoreCategory extends EntityPathBase<StoreCategory> {

    private static final long serialVersionUID = 280726491L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoreCategory storeCategory = new QStoreCategory("storeCategory");

    public final NumberPath<Long> categoryId = createNumber("categoryId", Long.class);

    public final StringPath categoryName = createString("categoryName");

    public final StringPath iconUrl = createString("iconUrl");

    public final QStoreCategory parentCategory;

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    public final ListPath<StoreCategory, QStoreCategory> subCategories = this.<StoreCategory, QStoreCategory>createList("subCategories", StoreCategory.class, QStoreCategory.class, PathInits.DIRECT2);

    public QStoreCategory(String variable) {
        this(StoreCategory.class, forVariable(variable), INITS);
    }

    public QStoreCategory(Path<? extends StoreCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoreCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoreCategory(PathMetadata metadata, PathInits inits) {
        this(StoreCategory.class, metadata, inits);
    }

    public QStoreCategory(Class<? extends StoreCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parentCategory = inits.isInitialized("parentCategory") ? new QStoreCategory(forProperty("parentCategory"), inits.get("parentCategory")) : null;
    }

}

