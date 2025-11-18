package com.reserve.illoomung.core.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserProfile is a Querydsl query type for UserProfile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserProfile extends EntityPathBase<UserProfile> {

    private static final long serialVersionUID = 955111825L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserProfile userProfile = new QUserProfile("userProfile");

    public final com.reserve.illoomung.core.auditing.QBaseTimeEntity _super = new com.reserve.illoomung.core.auditing.QBaseTimeEntity(this);

    public final QAccount account;

    public final StringPath addressFull = createString("addressFull");

    public final StringPath addressSido = createString("addressSido");

    public final StringPath addressSigungu = createString("addressSigungu");

    public final StringPath ageGroup = createString("ageGroup");

    public final StringPath birthMD = createString("birthMD");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath gender = createString("gender");

    public final EnumPath<com.reserve.illoomung.core.domain.entity.enums.GenderStat> genderStat = createEnum("genderStat", com.reserve.illoomung.core.domain.entity.enums.GenderStat.class);

    public final StringPath name = createString("name");

    public final StringPath nickName = createString("nickName");

    public final StringPath nicknameHash = createString("nicknameHash");

    public final StringPath phone = createString("phone");

    public final StringPath phoneHash = createString("phoneHash");

    public final BooleanPath phoneVerified = createBoolean("phoneVerified");

    public final StringPath profileImageUrl = createString("profileImageUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserProfile(String variable) {
        this(UserProfile.class, forVariable(variable), INITS);
    }

    public QUserProfile(Path<? extends UserProfile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserProfile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserProfile(PathMetadata metadata, PathInits inits) {
        this(UserProfile.class, metadata, inits);
    }

    public QUserProfile(Class<? extends UserProfile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new QAccount(forProperty("account")) : null;
    }

}

