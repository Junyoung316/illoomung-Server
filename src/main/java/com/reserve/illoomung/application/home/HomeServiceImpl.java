package com.reserve.illoomung.application.home;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    @Override
    public String homeInit() {
        return "Hello World!";
    }

}
