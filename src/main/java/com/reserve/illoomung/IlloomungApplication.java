package com.reserve.illoomung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.security.NoSuchAlgorithmException;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
public class IlloomungApplication {

	public static void main(String[] args) throws NoSuchAlgorithmException {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        SpringApplication.run(IlloomungApplication.class, args);

//        SecretKey key = Jwts.SIG.HS256.key().build();
//
//        // 2. 키를 Base64 문자열로 인코딩
//        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
//
//        System.out.println("✅ 256비트(32바이트) JWT 키 생성 완료.");
//        System.out.println("아래 라인을 복사하여 application.properties에 붙여넣으세요.");
//        System.out.println("---------------------------------------------------------------");
//
//        // ❗️ application.properties에서 사용하는 키 이름으로 변경하세요.
//        // 예: jwt.secret-key= (또는 jwt.secret=)
//        System.out.println("jwt.secret-key=" + base64Key);
//
//        System.out.println("---------------------------------------------------------------");
//        System.out.println("(참고: 생성된 키의 실제 길이: " + key.getEncoded().length + " bytes)");
    }
}
