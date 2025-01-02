-- 작품 테이블
CREATE TABLE work
(
    work_id       BIGSERIAL PRIMARY KEY,
    title         VARCHAR(255) NOT NULL,
    description   TEXT,
    author        VARCHAR(100),
    is_active     BOOLEAN                  DEFAULT true,
    is_adult_only BOOLEAN                  DEFAULT false,
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_work_title ON work (title);
