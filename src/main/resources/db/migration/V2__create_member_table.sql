-- 사용자 테이블
CREATE TABLE member
(
    member_id      BIGSERIAL PRIMARY KEY,
    email          VARCHAR(255) UNIQUE NOT NULL,
    username       VARCHAR(100)        NOT NULL,
    password       VARCHAR(255)        NOT NULL,
    adult_verified BOOLEAN                  DEFAULT false,
    created_at     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_member_username ON member (username);
