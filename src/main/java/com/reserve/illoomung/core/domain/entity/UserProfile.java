package com.reserve.illoomung.core.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

import com.reserve.illoomung.core.domain.entity.enums.GenderStat;

@Entity
@Table(name = "user_profile",
       indexes = {
           @Index(name = "idx_birthday_event", columnList = "birth_month, birth_day"),
           @Index(name = "idx_address", columnList = "address_sido, address_sigungu"),
           @Index(name = "idx_sigungu", columnList = "address_sigungu")
       }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false, unique = true,
                foreignKey = @ForeignKey(name = "fk_user_account"))
    private Account account;

    @Lob
    @Column(name = "phone")
    private String phone;  // 암호화된 전화번호

    @Column(name = "phone_hash", length = 64, unique = true)
    private String phoneHash;

    @Column(name = "phone_verified")
    @Builder.Default
    private Boolean phoneVerified = false;

    @Lob
    @Column(name = "name", nullable = false)
    private String name;   // 암호화된 이름

    @Lob
    @Column(name = "nickname", nullable = false)
    private String nickName;  // 암호화된 닉네임

    @Column(name = "nickname_hash", length = 64, nullable = false, unique = true)
    private String nicknameHash;

    @Lob
    @Column(name = "gender")
    private String gender;  // 암호화된 성별

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_stat", nullable = false)
    @Builder.Default
    private GenderStat genderStat = GenderStat.UNKNOWN;

    @Lob
    @Column(name = "birthday")
    private String birth;  // 암호화된 생일

    @Column(name = "birth_month")
    private Integer birthMonth;

    @Column(name = "birth_day")
    private Integer birthDay;

    @Lob
    @Column(name = "address_full", nullable = false)
    private String addressFull;  // 암호화된 전체 주소

    @Column(name = "address_sido", length = 50, nullable = false)
    private String addressSido;

    @Column(name = "address_sigungu", length = 50, nullable = false)
    private String addressSigungu;

    @Lob
    @Column(name = "profile_image_url")
    private String profileImageUrl;
}
