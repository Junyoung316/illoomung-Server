package com.reserve.illoomung.application.user.profile;

import com.reserve.illoomung.dto.request.auth.ChangePasswordRequest;
import com.reserve.illoomung.dto.request.auth.ProfileRequest;

public interface UserProfileService {
    void patchProfile(ProfileRequest request);
    void changePassword(ChangePasswordRequest request);
}
