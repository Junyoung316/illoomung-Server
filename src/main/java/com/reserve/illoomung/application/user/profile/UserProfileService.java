package com.reserve.illoomung.application.user.profile;

import com.reserve.illoomung.dto.request.auth.ChangePasswordRequest;
import com.reserve.illoomung.dto.request.auth.ProfileRequest;
import com.reserve.illoomung.dto.user.profile;

public interface UserProfileService {
    profile getProfile();
    void patchProfile(ProfileRequest request);
    void changePassword(ChangePasswordRequest request);
}
