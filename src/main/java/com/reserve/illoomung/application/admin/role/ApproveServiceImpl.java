package com.reserve.illoomung.application.admin.role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApproveServiceImpl implements ApproveService {

    private boolean checkRole

    @Override
    @Transactional
    public void approveUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authenticated: {}", authentication);
        if (authentication != null && authentication.isAuthenticated()) {
            String userId = authentication.getName();  // 사용자 식별자(ID) 조회
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            log.info("authenticated id: {}", userId);
            log.info("authenticated role: {}", role);

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                // 승인 로직
            } else {
                throw new IllegalStateException("접근 권한이 없습니다.");
            }
        }
    }

    @Override
    @Transactional
    public void rejectUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authenticated: {}", authentication);
        if (authentication != null && authentication.isAuthenticated()) {
            String userId = authentication.getName();  // 사용자 식별자(ID) 조회
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            log.info("authenticated id: {}", userId);
            log.info("authenticated role: {}", role);

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                // 승인 로직
            } else {
                throw new IllegalStateException("접근 권한이 없습니다.");
            }
        }
    }

}
