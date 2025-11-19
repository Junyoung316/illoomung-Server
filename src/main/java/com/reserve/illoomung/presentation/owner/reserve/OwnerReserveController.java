package com.reserve.illoomung.presentation.owner.reserve;

import com.reserve.illoomung.application.owner.reserve.OwnerReserveService;
import com.reserve.illoomung.core.dto.MainResponse;
import com.reserve.illoomung.domain.entity.enums.ReservationStatus;
import com.reserve.illoomung.dto.reserve.owner.OwnerGetReserveInfoResponse;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business/reserve")
@RequiredArgsConstructor
@Slf4j
public class OwnerReserveController {

    private final OwnerReserveService ownerReserveService;

    @GetMapping("/{storeId}/all")
    public ResponseEntity<MainResponse<List<OwnerGetReserveInfoResponse>>> getAllReserve(@Valid @PathVariable("storeId") Long storeId) {
        return ResponseEntity.ok(MainResponse.success(ownerReserveService.getReserveInfos(storeId)));
    }

    @PatchMapping("/{storeId}/{reserveId}/patch")
    public ResponseEntity<MainResponse<String>> patchReserve(@Valid @PathVariable("storeId") Long storeId, @Valid @PathVariable("reserveId") Long reserveId, @Param("status")ReservationStatus status) {
        ownerReserveService.patchReserve(storeId, reserveId, status);
        return ResponseEntity.ok(MainResponse.success());
    }

}
