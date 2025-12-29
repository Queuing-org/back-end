package queuing.core.friend.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import queuing.core.friend.application.dto.FriendSummary;
import queuing.core.friend.application.dto.GetFriendListCommand;
import queuing.core.friend.application.usecase.DeleteFriendUseCase;
import queuing.core.friend.application.usecase.GetFriendListUseCase;
import queuing.core.friend.presentation.response.FriendResponse;
import queuing.core.friend.presentation.response.ListFriendResponse;
import queuing.core.global.dto.SliceResult;
import queuing.core.global.response.ResponseBody;
import queuing.core.global.security.UserPrincipal;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {

    private final DeleteFriendUseCase deleteFriendUseCase;
    private final GetFriendListUseCase getFriendListUseCase;

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{targetSlug}")
    public ResponseEntity<ResponseBody<Boolean>> deleteFriend(
        @AuthenticationPrincipal UserPrincipal principal,
        @PathVariable String targetSlug
    ) {
        deleteFriendUseCase.deleteFriend(principal.getUsername(), targetSlug);
        return ResponseEntity.ok(ResponseBody.success(true));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<ResponseBody<ListFriendResponse>> getFriends(
        @AuthenticationPrincipal UserPrincipal principal,
        @RequestParam(required = false) Long lastId,
        @RequestParam(defaultValue = "20") int size
    ) {
        SliceResult<FriendSummary> result = getFriendListUseCase.getFriendList(
            new GetFriendListCommand(principal.getUsername(), lastId, size)
        );

        List<FriendResponse> items = result.items().stream()
            .map(FriendResponse::from)
            .toList();

        return ResponseEntity.ok(ResponseBody.success(
            ListFriendResponse.of(items, result.hasNext())
        ));
    }
}
