package com.reserve.illoomung.dto.response.verification.oauth.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoResponse {

    private Long id;

    @JsonProperty("connected_at")
    private String connectedAt;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    private Properties properties;

    private ForPartner forPartner;

    // getter, setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccount {

        @JsonProperty("profile_nickname_needs_agreement")
        private boolean profileNicknameNeedsAgreement;

        @JsonProperty("profile_image_needs_agreement")
        private boolean profileImageNeedsAgreement;

        private Profile profile;

        @JsonProperty("name_needs_agreement")
        private boolean nameNeedsAgreement;

        private String name;

        @JsonProperty("has_email")
        private boolean hasEmail;

        @JsonProperty("email_needs_agreement")
        private boolean emailNeedsAgreement;

        @JsonProperty("is_email_valid")
        private boolean isEmailValid;

        @JsonProperty("is_email_verified")
        private boolean isEmailVerified;

        private String email;

        @JsonProperty("age_range_needs_agreement")
        private boolean ageRangeNeedsAgreement;

        @JsonProperty("age_range")
        private String ageRange;

        @JsonProperty("birthyear_needs_agreement")
        private boolean birthyearNeedsAgreement;

        private String birthyear;

        @JsonProperty("birthday_needs_agreement")
        private boolean birthdayNeedsAgreement;

        private String birthday;

        @JsonProperty("birthday_type")
        private String birthdayType;

        @JsonProperty("is_leap_month")
        private boolean isLeapMonth;

        @JsonProperty("gender_needs_agreement")
        private boolean genderNeedsAgreement;

        private String gender;

        @JsonProperty("phone_number_needs_agreement")
        private boolean phoneNumberNeedsAgreement;

        @JsonProperty("phone_number")
        private String phoneNumber;

        @JsonProperty("ci_needs_agreement")
        private boolean ciNeedsAgreement;

        private String ci;

        @JsonProperty("ci_authenticated_at")
        private String ciAuthenticatedAt;

        // getter, setter
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Profile {

        private String nickname;

        @JsonProperty("thumbnail_image_url")
        private String thumbnailImageUrl;

        @JsonProperty("profile_image_url")
        private String profileImageUrl;

        @JsonProperty("is_default_image")
        private boolean isDefaultImage;

        @JsonProperty("is_default_nickname")
        private boolean isDefaultNickname;

        // getter, setter
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Properties {

        @JsonProperty("${CUSTOM_PROPERTY_KEY}")
        private String customPropertyValue;

        // 필요시 추가 필드들

        // getter, setter
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForPartner {

        private String uuid;

        // getter, setter
    }

    // getters, setters for KakaoUserResponse fields
}
