package com.reserve.illoomung.presentation.admin.role;

import com.reserve.illoomung.application.admin.role.ApproveService;
import com.reserve.illoomung.core.dto.MainResponse;
import com.reserve.illoomung.dto.admin.role.RejectReasonDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class ApproveRoleController {

    private final ApproveService approveService;

    @PatchMapping("/onwer/approve/{requestId}")
    public ResponseEntity<MainResponse<String>> approveRole(@PathVariable("requestId") Long id) {
        // 권한 상승 요청 승인
        approveService.approveUserRole(id);
        return ResponseEntity.ok(MainResponse.success());
    }


    @PatchMapping("/onwer/reject/{requestId}")
    public ResponseEntity<MainResponse<String>> rejectRole(@PathVariable("requestId") Long id, @RequestBody RejectReasonDto rejectReasonDto) {
        // 권한 상승 요청 반려
        approveService.rejectUserRole(id, rejectReasonDto);
        return ResponseEntity.ok(MainResponse.success());
    }

}
