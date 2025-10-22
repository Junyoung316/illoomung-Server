package com.reserve.illoomung.application.admin.role;

import com.reserve.illoomung.dto.admin.role.RejectReasonDto;

public interface ApproveService {
    void approveUserRole(Long requestId); // 권한 상승 승인 처리 로직
    void rejectUserRole(Long requestId, RejectReasonDto rejectReasonDto); // 권한 상승 반려 처리 로직
}
