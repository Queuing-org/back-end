package queuing.core.room.presentation.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;

public record CreateRoomRequest(
    @NotBlank
    String title,
    String password,
    Set<String> tags
) {
}
