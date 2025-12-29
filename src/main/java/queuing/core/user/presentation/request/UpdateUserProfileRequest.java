package queuing.core.user.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserProfileRequest(
    @NotBlank(message = "닉네임은 빈 값일 수 없어요.")
    @Size(min = 2, max = 20, message = "닉네임은 2 ~ 20자 사이여야 해요.")
    String nickname
) {
}
