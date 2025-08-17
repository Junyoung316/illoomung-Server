package com.reserve.illoomung.domain.service;

import org.springframework.stereotype.Service;

import com.reserve.illoomung.core.domain.entity.enums.SocialProvider;
import com.reserve.illoomung.core.domain.repository.AccountRepository;
import com.reserve.illoomung.core.exception.DuplicateException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterValidator {

    private final AccountRepository accountRepository;
    
    /**
     * 이메일 중복 검사를 수행합니다.
     * 
     * @param emailHash 검사할 이메일 해시값
     * 
     * @throws DuplicateException 이메일이 중복된 경우
     */
    public void validateEmailDuplicate(String emailHash) {
        if (accountRepository.findByEmailHash(emailHash).isPresent()) {
            throw new DuplicateException("이미 존재하는 이메일입니다: " + emailHash);
        }
    }

    /**
     * 소셜 아이디 중복 검사를 수행합니다.
     * 
     * @param socialProvider 검사할 소셜 제공자
     * @param socialIdHash 검사할 소셜 아이디 해시값
     * 
     * @throws DuplicateException 이메일이 중복된 경우
     */
    public void validateSocialDuplicate(SocialProvider socialProvider, String socialIdHash) {
        boolean exists = accountRepository.existsBySocialProviderAndSocialId(socialProvider, socialIdHash);
            
        if (exists) {
            throw new DuplicateException("이미 존재하는 소셜 아이디입니다: " + socialIdHash);
        }
    }

    // /**
    //  * 전화번호 중복 검사를 수행합니다.
    //  * 
    //  * @param phone 검사할 전화번호
    //  * @throws DuplicatePhoneException 전화번호가 중복된 경우
    //  */
    // public void validatePhoneDuplicate(String phone) {
    //     if (userProfileRepository.findAll().stream().anyMatch(p -> phone.equals(p.getPhone()))) {
    //         throw new DuplicatePhoneException("이미 존재하는 전화번호입니다: " + phone);
    //     }
    // }
}
