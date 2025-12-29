package queuing.core.friend.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import queuing.core.friend.application.usecase.AcceptFriendRequestUseCase;
import queuing.core.friend.application.usecase.SendFriendRequestUseCase;
import queuing.core.friend.presentation.request.SendFriendRequest;
import queuing.core.global.response.ResponseBody;
import queuing.core.global.security.UserPrincipal;

@RestController
@RequestMapping("/api/v1/friend-requests")
@RequiredArgsConstructor
public class FriendRequestController {

    private final SendFriendRequestUseCase sendFriendRequestUseCase;
    private final AcceptFriendRequestUseCase acceptFriendRequestUseCase;

    @PostMapping
    public ResponseEntity<ResponseBody<Boolean>> sendRequest(
        @AuthenticationPrincipal UserPrincipal principal,
        @Validated @RequestBody SendFriendRequest request
    ) {
        sendFriendRequestUseCase.sendRequest(principal.getUsername(), request.targetSlug());
        return ResponseEntity.ok(ResponseBody.success(true));
    }

    @PatchMapping("/{requestId}")
    public ResponseEntity<ResponseBody<Boolean>> acceptRequest(
        @AuthenticationPrincipal UserPrincipal principal,
        @PathVariable Long requestId
    ) {
        acceptFriendRequestUseCase.acceptRequest(principal.getUsername(), requestId);
        return ResponseEntity.ok(ResponseBody.success(true));
    }
}
