package queuing.core.room.application.model;

import java.util.Set;

import io.undertow.util.BadRequestException;

public record CreateRoomCommand(
    String title,
    String password,
    Set<String> slugs
) {
    public CreateRoomCommand {
        title = (title != null) ? title.trim() : null;
        password = (password != null) ? password.trim() : null;
        slugs = (slugs != null) ? slugs : Set.of();
    }

    public void validate() throws BadRequestException {
        if (title == null || title.isBlank()) {
            throw new BadRequestException("방 이름은 필수 항목입니다.");
        }

        if (slugs.size() > 5) {
            throw new BadRequestException("태그는 최대 5개까지 선택할 수 있습니다.");
        }
    }
}
