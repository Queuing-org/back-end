-- 사용자
CREATE TABLE `users`
(
    -- - 주요 키
    `id`                      bigint       NOT NULL AUTO_INCREMENT,
    -- - 슬러그
    `slug`                    varchar(255) NOT NULL,
    -- - 닉네임
    `nickname`                varchar(255) DEFAULT NULL,
    -- - 프로필 사진 URL
    `profile_image_url`       varchar(255) DEFAULT NULL,
    -- - 소셜 로그인 제공자
    `provider`                varchar(255) NOT NULL,
    -- - 소셜 로그인 제공자의 사용자 구분 키
    `provider_id`             varchar(255) NOT NULL,
    -- - 사용자 생성일
    `created_at`              datetime(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    -- - 사용자 정보 갱신일
    `updated_at`              datetime(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    -- - 사용자 삭제일
    `deleted_at`              datetime(6)  DEFAULT NULL,
    -- - 주요 키 설정
    PRIMARY KEY (`id`),
    -- - 유일 키 설정
    -- -- 슬러그
    CONSTRAINT `uk_users_slug` UNIQUE (`slug`),
    -- -- 사용자 닉네임
    CONSTRAINT `uk_users_nickname` UNIQUE (`nickname`),
    -- -- 소셜 로그인 정보
    CONSTRAINT `uk_users_provider_and_provider_id` UNIQUE (`provider`, `provider_id`)
    -- - 추가 인덱스 설정
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 방 분위기
CREATE TABLE `music_tags`
(
    -- - 주요 키
    `id`                      bigint       NOT NULL AUTO_INCREMENT,
    -- - 슬러그
    `slug`                    varchar(255) NOT NULL,
    -- - 표시 이름
    `name`                    varchar(255) NOT NULL,
    -- - 주요 키 설정
    PRIMARY KEY (`id`),
    -- - 유일 키 설정
    CONSTRAINT `uk_music_tags_slug` UNIQUE (`slug`)
    -- - 추가 인덱스 설정
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 방
CREATE TABLE `rooms`
(
    -- - 주요 키
    `id`                        bigint       NOT NULL AUTO_INCREMENT,
    -- - 슬러그
    `slug`                      varchar(64)  NOT NULL,
    -- - 방 제목
    `title`                     varchar(255) NOT NULL,
    -- - 비밀 방 여부
    `is_private`                tinyint(1)   NOT NULL DEFAULT 0,
    -- - 방 비밀번호
    `password`                  varchar(255) DEFAULT NULL,
    -- - 방 소유자
    `owner_id`                  bigint       NOT NULL,
    -- - 최대 수용 인원
    `max_listeners`             int          NOT NULL DEFAULT 25,
    -- - 방 생성일
    `created_at`                datetime(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    -- - 방 정보 갱신일
    `updated_at`                datetime(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    -- - 주요 키 설정
    PRIMARY KEY (`id`),
    -- - 유일 키 설정
    -- -- 방 슬러그
    CONSTRAINT `uk_rooms_slug` UNIQUE (`slug`),
    -- - 추가 인덱스 설정
    -- -- 방 소유자
    CONSTRAINT `fk_rooms_owner_id` FOREIGN KEY (`owner_id`) REFERENCES users(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_rooms_on_is_private_and_created_at_desc ON rooms (is_private, created_at DESC);
CREATE INDEX idx_rooms_on_owner_id_and_created_at_desc ON rooms (owner_id, created_at DESC);

-- 방과 태그 간 매핑
CREATE TABLE rooms_music_tags (
    -- - 방 주요 키
    `room_id`                   bigint NOT NULL,
    -- - 태그 주요 키
    `tag_id`                    bigint NOT NULL,
    -- - 주요 키 설정
    PRIMARY KEY (`room_id`, `tag_id`),
    -- - 외래 키 설정
    CONSTRAINT `fk_rooms_music_tags_room_id` FOREIGN KEY (`room_id`) REFERENCES rooms(`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_rooms_music_tags_tag_id` FOREIGN KEY (`tag_id`) REFERENCES music_tags(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_rooms_music_tags_on_tag_id_and_room_id ON rooms_music_tags (tag_id, room_id);
