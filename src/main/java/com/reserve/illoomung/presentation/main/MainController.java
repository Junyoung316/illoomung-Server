package com.reserve.illoomung.presentation.main;

import com.reserve.illoomung.application.main.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final MainService homeService;

    @GetMapping("/item") // 메인 페이지 데이터 전송
    public String home() {
        return homeService.mainInit();
    }

    // 상세 페이지 정보

}
 /*
 * [대표 이미지]
 * 업체 이름
 * ⭐ 4.8 (120) · 500m
 * #대형견가능 #주차가능
 * ₩₩ · 영업 중
 * 오늘 오후 2시 예약 가능
 */