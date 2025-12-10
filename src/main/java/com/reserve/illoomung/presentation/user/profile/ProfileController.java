package com.reserve.illoomung.presentation.user.profile;

import com.reserve.illoomung.application.user.profile.UserProfileService;
import com.reserve.illoomung.core.dto.MainResponse;
import com.reserve.illoomung.dto.request.auth.ChangePasswordRequest;
import com.reserve.illoomung.dto.request.auth.ProfileRequest;
import com.reserve.illoomung.dto.user.profile;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jdt.internal.compiler.batch.Main;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<MainResponse<profile>> profile() {
        return ResponseEntity.ok(MainResponse.success(userProfileService.getProfile()));
    }

    @PatchMapping("/patch")
    public ResponseEntity<MainResponse<String>> patchProfile(@Valid @RequestBody ProfileRequest request) {
        userProfileService.patchProfile(request);
        return ResponseEntity.ok(MainResponse.success());
    }

    @PatchMapping("/password")
    public ResponseEntity<MainResponse<String>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userProfileService.changePassword(request);
        return ResponseEntity.ok(MainResponse.success());
    }
}
