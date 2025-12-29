package queuing.core.room.presentation.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRoomRequest(
    @NotBlank(message = "제목은 빈 값일 수 없어요.")
    @Size(max = 255, message = "제목이 너무 길어요.")
    String title,
    @Size(max = 255, message = "비밀번호가 너무 길어요.")
    String password,
    @Size(max = 5, message = "태그가 너무 많아요.")
    Set<String> tags
) {
}
