package queuing.core.user.application;

public record UpdateNicknameCommand(
    String userSlug,
    String nickname
) {
}
