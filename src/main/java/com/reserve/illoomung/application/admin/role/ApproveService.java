package com.reserve.illoomung.application.admin.role;

public interface ApproveService {
    void approveUserRole(); // 권한 상승 승인 처리 로직
    void rejectUserRole(); // 권한 상승 반려 처리 로직
}
