package queuing.core.room.application.model;

public enum TagMatchType {
    ALL,
    ANY;

    public static TagMatchType from(String value) {
        if (value == null || value.isBlank()) {
            return ANY;
        }

        try {
            return TagMatchType.valueOf(value.strip().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("지원하지 않는 태그 매칭 방식입니다.");
        }
    }
}
