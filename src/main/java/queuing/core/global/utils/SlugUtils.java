package queuing.core.global.utils;

import java.security.SecureRandom;

public final class SlugUtils {
    private SlugUtils() {}

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int DEFAULT_LENGTH = 8;

    public static String generateSlug() {
        return generateSlug(DEFAULT_LENGTH);
    }

    public static String generateSlug(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Slug length must be positive");
        }

        StringBuilder slug = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            slug.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return slug.toString();
    }
}
