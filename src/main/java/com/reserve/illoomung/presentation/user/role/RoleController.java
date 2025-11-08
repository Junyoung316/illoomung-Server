package com.reserve.illoomung.presentation.user.role;

import com.reserve.illoomung.application.user.role.RoleService;
import com.reserve.illoomung.core.dto.MainResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/onwer")
    public ResponseEntity<MainResponse<String>> createRole() {
        roleService.requestOwnerRegistration();
        return ResponseEntity.ok(MainResponse.created());
    }
}

