package com.reserve.illoomung.presentation.home;

import com.reserve.illoomung.application.home.HomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public String home() {
        return homeService.homeInit();
    }

}
 /*
 * [대표 이미지]
 * 업체 이름
 * ⭐ 4.8 (120) · 500m
 * #대형견가능 #주차가능
 * ₩₩ · 영업 중
 * 오늘 오후 2시 예약 가능
 */