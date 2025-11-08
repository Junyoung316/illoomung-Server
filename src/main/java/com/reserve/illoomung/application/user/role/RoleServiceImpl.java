package com.reserve.illoomung.application.user.role;

import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.entity.RoleUpgradeRequests;
import com.reserve.illoomung.core.domain.entity.enums.Role;
import com.reserve.illoomung.core.domain.repository.AccountRepository;
import com.reserve.illoomung.core.domain.repository.RoleUpgradeRequestsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final AccountRepository accountRepository;
    private final RoleUpgradeRequestsRepository roleUpgradeRequestsRepository;

    // TODO: 사업자등록번호 테이블 추가 시 매개변수 추가
    @Override
    @Transactional
    public void requestOwnerRegistration() { // 권한 변경(user -> 임시 owner), 신청 테이블에 사용자 등록
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authenticated: {}", authentication);
        if (authentication != null && authentication.isAuthenticated()) {
            String userId = authentication.getName();  // 사용자 식별자(ID) 조회
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            log.info("authenticated id: {}", userId);
            log.info("authenticated role: {}", role);

            boolean isUSER = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));

            if (isUSER) {
                Account account = accountRepository.findByAccountId(Long.valueOf(userId))
                        .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
                account.setRole(Role.PRE_OWNER);
                accountRepository.save(account);
                // 권산 상승 테이블 저장
                RoleUpgradeRequests roleUpgradeRequests = RoleUpgradeRequests.builder()
                        .account(account)
                        .build();

                roleUpgradeRequestsRepository.save(roleUpgradeRequests);
                log.info("save");
            } else {
                throw new IllegalStateException("이미 권한을 가진 사용자입니다.");
            }
        }
    }
}
