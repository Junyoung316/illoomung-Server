package com.reserve.illoomung.core.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.reserve.illoomung.core.dto.CryptoResult;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class SecurityUtil {

    private static final int IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

    /**
     * 현재 로그인된 사용자의 인증 정보를 반환합니다.
     * @return Authentication 객체 (로그인되어 있지 않으면 null 또는 AnonymousAuthenticationToken)
     */
    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 현재 로그인된 사용자의 사용자명 (principal)을 반환합니다.
     * @return 로그인된 사용자의 사용자명 (일반적으로 이메일 또는 ID), 로그인되어 있지 않으면 "anonymousUser" 또는 null
     */
    public static String getCurrentUsername() {
        Authentication authentication = getCurrentAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser")) {
            return null; // 로그인되어 있지 않거나 익명 사용자
        }
        // UserDetails 객체에서 사용자명을 가져옵니다.
        if (authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        // 그 외의 경우 (예: String principal)
        return authentication.getPrincipal().toString();
    }

    /**
     * 현재 사용자가 로그인되어 있는지 여부를 확인합니다.
     * @return 로그인되어 있으면 true, 아니면 false
     */
    public static boolean isAuthenticated() {
        Authentication authentication = getCurrentAuthentication();
        // authentication이 null이 아니고, 인증되어 있으며, 익명 사용자가 아닌 경우
        return authentication != null && authentication.isAuthenticated() &&
               !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"));
    }

    // AES-GCM 암호화
    public static String encrypt(String plainText, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // AES-GCM 복호화
    public static String decrypt(String cipherText, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    // SHA-256 해시
    public static String hash(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashBytes);
    }

    // 원하는 크기(keySize)의 **AES 대칭 키(SecretKey)**를 생성하는 함수
    public static SecretKey generateKey(int keySize) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(keySize);
        return keyGen.generateKey();
    }

    // **IV(Initialization Vector, 초기화 벡터)**를 랜덤하게 생성하는 함수
    public static byte[] generateIV() {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public String textDecrypt(String cipherText) {
        try { // TODO: 초기화 백처 추출해 사용해야함
            SecretKey key = generateKey(128);
            byte[] iv = generateIV();
            return decrypt(cipherText, key, iv);
        } catch(Exception e) {
            return null;
        }
    }

    public CryptoResult cryptoResult(String plainText) {
        try {
            SecretKey key = generateKey(128);
            byte[] iv = generateIV();

             // 암호화
            String encryptedText = encrypt(plainText, key, iv);

            // 해시 생성 (원문 기반으로 생성) 검색용
            String hash = hash(plainText);

            return new CryptoResult(encryptedText, hash);
        } catch(Exception e) {
            return new CryptoResult(null, null);
        }
    }
}