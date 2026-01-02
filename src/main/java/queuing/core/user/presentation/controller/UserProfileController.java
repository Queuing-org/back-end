package queuing.core.user.presentation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import queuing.core.global.response.ResponseBody;
import queuing.core.global.security.authorization.UserPrincipal;
import queuing.core.user.application.command.UpdateNicknameCommand;
import queuing.core.user.application.dto.UserProfileDto;
import queuing.core.user.application.usecase.GetUserProfileUseCase;
import queuing.core.user.application.usecase.UpdateUserProfileUseCase;
import queuing.core.user.presentation.request.UpdateUserProfileRequest;
import queuing.core.user.presentation.response.UserProfileResponse;

@RestController
@RequestMapping("/api/v1/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {
    private final GetUserProfileUseCase getUserProfileUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseBody<UserProfileResponse>> getMyProfile(
        @AuthenticationPrincipal UserPrincipal principal
    ) {
        UserProfileDto userProfile = getUserProfileUseCase.getUserProfile(principal.getUsername());
        return ResponseEntity.ok(ResponseBody.success(UserProfileResponse.from(userProfile)));
    }

    @GetMapping("/{slug}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseBody<UserProfileResponse>> getUserProfile(
        @PathVariable String slug
    ) {
        UserProfileDto userProfile = getUserProfileUseCase.getUserProfile(slug);
        return ResponseEntity.ok(ResponseBody.success(UserProfileResponse.from(userProfile)));
    }

    @GetMapping("/check-nickname")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseBody<Boolean>> checkNickname(
        @NotBlank(message = "닉네임은 빈 값일 수 없어요.")
        @Size(min = 2, max = 20, message = "닉네임은 2 ~ 20자 사이여야 해요.")
        @Valid @RequestParam String nickname
    ) {
        return ResponseEntity.ok(ResponseBody.success(getUserProfileUseCase.isNickname(nickname)));
    }

    @PatchMapping("/me/onboarding")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseBody<Boolean>> completeOnboarding(
        @AuthenticationPrincipal UserPrincipal principal,
        @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        updateUserProfileUseCase.completeOnboarding(new UpdateNicknameCommand(principal.getUsername(), request.nickname()));
        return ResponseEntity.ok(ResponseBody.success(true));
    }

    @PatchMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseBody<Boolean>> updateProfile(
        @AuthenticationPrincipal UserPrincipal principal,
        @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        updateUserProfileUseCase.updateUserProfile(new UpdateNicknameCommand(principal.getUsername(), request.nickname()));
        return ResponseEntity.ok(ResponseBody.success(true));
    }
}
