-- 작품 테이블
CREATE TABLE work
(
    work_id       BIGSERIAL PRIMARY KEY,
    title         VARCHAR(255)                           NOT NULL,
    description   TEXT,
    author        VARCHAR(100)                           NOT NULL,
    is_active     BOOLEAN                  DEFAULT true  NOT NULL,
    is_adult_only BOOLEAN                  DEFAULT false NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_title_length CHECK (LENGTH(TRIM(title)) >= 1),
    CONSTRAINT chk_author_length CHECK (LENGTH(TRIM(author)) >= 1)
);

CREATE INDEX idx_work_title ON work (title);
CREATE INDEX idx_work_author ON work (author);
CREATE INDEX idx_work_status ON work (is_active, is_adult_only);
