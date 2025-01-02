-- 작품 가격 정책 테이블 (이벤트 기간 관리)
CREATE TABLE work_pricing_policy
(
    work_pricing_policy_id BIGSERIAL PRIMARY KEY,
    work_id                BIGINT                   NOT NULL,
    is_free                BOOLEAN                  NOT NULL,
    price                  DECIMAL(10, 2),
    start_date             TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date               TIMESTAMP WITH TIME ZONE,
    created_at             TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT valid_date_range CHECK (end_date IS NULL OR start_date < end_date),
    CONSTRAINT fk_work_pricing_policy_work FOREIGN KEY (work_id)
        REFERENCES work (work_id) ON DELETE CASCADE
);

CREATE INDEX idx_work_pricing_policy_work_id_date ON work_pricing_policy (work_id, start_date, end_date);
