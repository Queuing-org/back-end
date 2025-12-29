package queuing.core.friend.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record SendFriendRequest(
    @NotBlank(message = "친구 추가할 대상을 입력해주세요.")
    String targetSlug
) {}
