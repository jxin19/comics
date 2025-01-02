-- 사용자 테이블
CREATE TABLE member
(
    member_id      BIGSERIAL PRIMARY KEY,
    email          VARCHAR(120) NOT NULL,
    username       VARCHAR(100) NOT NULL,
    password       VARCHAR(255) NOT NULL,
    adult_verified BOOLEAN                  DEFAULT false,
    created_at     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_member_email UNIQUE (email),
    CONSTRAINT chk_username_length CHECK (LENGTH(username) >= 2)
);

CREATE INDEX idx_member_username ON member (username);
CREATE INDEX idx_member_email ON member (email);
