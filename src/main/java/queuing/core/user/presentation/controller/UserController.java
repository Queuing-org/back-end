package queuing.core.user.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import queuing.core.global.response.ResponseBody;
import queuing.core.global.security.UserPrincipal;
import queuing.core.user.application.AuthenticationService;
import queuing.core.user.application.UpdateNicknameCommand;
import queuing.core.user.presentation.request.NicknameUpdateRequest;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationService authenticationService;

    @PatchMapping("/nickname")
    public ResponseEntity<ResponseBody<Void>> updateNickname(
        @AuthenticationPrincipal UserPrincipal principal,
        @Validated @RequestBody NicknameUpdateRequest request
    ) {
        authenticationService.updateNickname(new UpdateNicknameCommand(principal.getUsername(), request.nickname()));
        return ResponseEntity.ok(ResponseBody.success());
    }
}

