package com.reserve.illoomung.core.util;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class SecurityUtil {

    private static final int IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

    private final SecretKey secretKey;

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

    // ⭐️ (시그니처 수정) encrypt 오버로딩: byte[] 반환
    public static byte[] encrypt(String plainText, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        return cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    }


    /**
     * ⭐️ 수정된 AES-GCM 복호화
     * @param cipherTextBytes Base64로 인코딩된 문자열이 아닌, **순수 암호문 바이트 배열**
     * @param key             비밀 키
     * @param iv              초기화 벡터
     * @return 복호화된 원본 문자열
     * @throws Exception
     */
    public static String decrypt(byte[] cipherTextBytes, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        // ⭐️ 수정: Base64 디코딩 제거 (이미 순수 byte[]를 받음)
        // byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        byte[] decrypted = cipher.doFinal(cipherTextBytes);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    // SHA-256 해시 (동일)
    public static String hash(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashBytes);
    }

    // 키 생성 (static 유지 - 키 생성 유틸로 사용)
    public static SecretKey generateKey(int keySize) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(keySize);
        return keyGen.generateKey();
    }

    // IV 생성 (static 유지)
    public static byte[] generateIV() {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    /**
     * ⭐️ 수정된 복호화 메소드
     */
    public String textDecrypt(String combinedBase64) {
        try {
            // ⭐️ 수정: 멤버 변수에 저장된 키 사용
            // SecretKey key = generateKey(128); // (X)

            // 1. Base64 디코딩
            // combinedBase64 -> [IV(12 bytes)] + [Ciphertext(가변 bytes)]
            byte[] combinedData = Base64.getDecoder().decode(combinedBase64);

            // 2. IV 추출 (앞 12바이트)
            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(combinedData, 0, iv, 0, IV_LENGTH);

            // 3. 실제 암호문 추출 (12바이트 이후 끝까지)
            int ciphertextLength = combinedData.length - IV_LENGTH;
            byte[] ciphertextBytes = new byte[ciphertextLength];
            System.arraycopy(combinedData, IV_LENGTH, ciphertextBytes, 0, ciphertextLength);

            // 4. 복호화
            // ⭐️ 수정: this.secretKey와 byte[]를 받는 decrypt 메소드 호출
            return decrypt(ciphertextBytes, this.secretKey, iv);

        } catch (Exception e) {
            e.printStackTrace(); // 실제 서비스에서는 로깅 프레임워크 사용
            return null;
        }
    }

    /**
     * ⭐️ 수정된 암호화 + 해시 생성 메소드
     */
    public CryptoResult cryptoResult(String plainText) {
        try {
            // ⭐️ 수정: 멤버 변수에 저장된 키 사용
            // SecretKey key = generateKey(128); // (X)

            byte[] iv = generateIV();

            // 1. 암호화 (byte[] 결과)
            byte[] encryptedBytes = encrypt(plainText, this.secretKey, iv);

            // 2. ⭐️ [IV] + [Ciphertext] 결합
            //
            byte[] combinedData = new byte[IV_LENGTH + encryptedBytes.length];
            System.arraycopy(iv, 0, combinedData, 0, IV_LENGTH);
            System.arraycopy(encryptedBytes, 0, combinedData, IV_LENGTH, encryptedBytes.length);

            // 3. 결합된 데이터를 Base64로 인코딩
            String encryptedBase64 = Base64.getEncoder().encodeToString(combinedData);

            // 4. 해시 생성 (원문 기반)
            String hash = hash(plainText);

            return new CryptoResult(encryptedBase64, hash);

        } catch(Exception e) {
            e.printStackTrace();
            return new CryptoResult(null, null);
        }
    }
}