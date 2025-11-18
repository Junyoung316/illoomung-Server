package com.reserve.illoomung.presentation.user.reserve;

import com.reserve.illoomung.application.user.reserve.UserReserveService;
import com.reserve.illoomung.core.dto.MainResponse;
import com.reserve.illoomung.dto.reserve.user.UserReserveGetResponse;
import com.reserve.illoomung.dto.reserve.user.UserReserveSaveRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/reserve")
@RequiredArgsConstructor
@Slf4j
public class UserReserveController {

    private final UserReserveService userReserveService;

    @GetMapping("/my-search") // 사용자 예약 조회
    public ResponseEntity<MainResponse<List<UserReserveGetResponse>>> getUserReserve() {
        return ResponseEntity.ok(MainResponse.success(userReserveService.getReserve()));
    }

    @PostMapping("/{storeId}/save") // 사용자 예약 등록
    public ResponseEntity<MainResponse<String>> saveUserReserve(@Valid @RequestBody UserReserveSaveRequest request, @PathVariable("storeId") Long id) {
        userReserveService.saveReserve(request, id);
        return ResponseEntity.ok(MainResponse.created());
    }

    @DeleteMapping("/{storeId}/{reserveId}/cancel")
    public ResponseEntity<MainResponse<String>> deleteUserReserve(@PathVariable("storeId") Long storeId, @PathVariable("reserveId") Long reserve) {
        userReserveService.cancelReserve(storeId, reserve);
        return ResponseEntity.ok(MainResponse.created());
    }

}
