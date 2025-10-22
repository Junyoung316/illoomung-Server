package com.reserve.illoomung.application.admin.role;

import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.entity.RoleUpgradeRequests;
import com.reserve.illoomung.core.domain.entity.enums.Role;
import com.reserve.illoomung.core.domain.entity.enums.RoleStatus;
import com.reserve.illoomung.core.domain.repository.AccountRepository;
import com.reserve.illoomung.core.domain.repository.RoleUpgradeRequestsRepository;
import com.reserve.illoomung.core.util.DateTimeUtils;
import com.reserve.illoomung.dto.admin.role.RejectReasonDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApproveServiceImpl implements ApproveService {

    private final RoleUpgradeRequestsRepository roleUpgradeRequestsRepository;
    private final AccountRepository accountRepository;

    private Long getCurrentAdminId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 1. 인증 정보가 없거나, 인증되지 않은 경우 명확한 예외를 던집니다.
        if (authentication == null || !authentication.isAuthenticated() || authentication.getName() == null) {
            // 혹은 인증 관련 Custom Exception을 사용하는 것이 더 좋습니다.
            throw new IllegalStateException("인증된 사용자 정보를 찾을 수 없습니다.");
        }

        String userIdStr = authentication.getName();
        try {
            // 2. String을 Long으로 변환하되, 실패할 경우를 대비합니다.
            return Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            // 로그를 남겨 어떤 ID가 문제였는지 추적할 수 있도록 합니다.
            // log.error("사용자 ID를 숫자로 변환할 수 없습니다: {}", userIdStr);
            throw new IllegalStateException("사용자 ID의 형식이 올바르지 않습니다.", e);
        }
    }

    private boolean checkRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authenticated: {}", authentication);
        if (authentication != null && authentication.isAuthenticated()) {
            String userId = authentication.getName();  // 사용자 식별자(ID) 조회
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            log.info("authenticated id: {}", userId);
            log.info("authenticated role: {}", role);

             return authentication.getAuthorities().stream()
                    .anyMatch( // 권환이 admin인지 확인
                            authority -> authority.getAuthority().equals("ROLE_ADMIN")
                    );
        }
        return false;
    }

    @Override
    @Transactional
    public void approveUserRole(Long requestId) {
        if (checkRole()) {
            // 승인 로직
            RoleUpgradeRequests roleUpgradeRequests = roleUpgradeRequestsRepository.findById(requestId)
                    .orElseThrow(() -> new RuntimeException("요청 데이터을 찾을 수 없습니다."));

            Account admin = accountRepository.findByAccountId(Objects.requireNonNull(getCurrentAdminId()))
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            roleUpgradeRequests.setAdmin(admin);
            roleUpgradeRequests.setStatus(RoleStatus.APPROVED);
            roleUpgradeRequests.setProcessedAt(DateTimeUtils.now());

            roleUpgradeRequestsRepository.save(roleUpgradeRequests);
        } else {
            throw new IllegalStateException("접근 권한이 없습니다.");
        }
    }

    @Override
    @Transactional
    public void rejectUserRole(Long requestId, RejectReasonDto rejectReasonDto) {
        if (checkRole()) {
            // 반려 로직
            RoleUpgradeRequests roleUpgradeRequests = roleUpgradeRequestsRepository.findById(requestId)
                    .orElseThrow(() -> new RuntimeException("요청 데이터을 찾을 수 없습니다."));

            Account admin = accountRepository.findByAccountId(Objects.requireNonNull(getCurrentAdminId()))
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            Account user = roleUpgradeRequests.getAccount();

            user.setRole(Role.OWNER);

            accountRepository.save(user);

            roleUpgradeRequests.setAdmin(admin);
            roleUpgradeRequests.setStatus(RoleStatus.REJECTED);
            roleUpgradeRequests.setRejectionReason(rejectReasonDto.getReason());
            roleUpgradeRequests.setProcessedAt(DateTimeUtils.now());

            roleUpgradeRequestsRepository.save(roleUpgradeRequests);

        } else {
            throw new IllegalStateException("접근 권한이 없습니다.");
        }
    }
}
