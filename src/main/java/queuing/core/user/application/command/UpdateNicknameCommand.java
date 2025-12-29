package queuing.core.user.application.command;

public record UpdateNicknameCommand(
    String userSlug,
    String nickname
) {
}
