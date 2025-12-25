package queuing.core.room.application.dto;

import java.util.Set;

import queuing.core.global.exception.BusinessException;
import queuing.core.global.exception.ErrorCode;

public record CreateRoomCommand(
    String userSlug,
    String title,
    String password,
    Set<String> slugs
) {
    public CreateRoomCommand {
        title = (title != null) ? title.trim() : null;
        password = (password != null) ? password.trim() : null;
        userSlug = (userSlug != null) ? userSlug.trim() : null;

        slugs = (slugs != null) ? slugs : Set.of();

        if (userSlug == null || userSlug.isBlank()) {
            throw new BusinessException(ErrorCode.USER_NOT_AUTHENTICATED);
        }

        if (title == null || title.isBlank()) {
            throw new BusinessException(ErrorCode.COMMON_INVALID_INPUT);
        }

        if (slugs.size() > 5) {
            throw new BusinessException(ErrorCode.COMMON_INVALID_INPUT);
        }
    }
}
