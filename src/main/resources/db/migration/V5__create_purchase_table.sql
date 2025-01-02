-- 구매 내역 테이블
CREATE TABLE purchase
(
    purchase_id  BIGSERIAL PRIMARY KEY,
    member_id    BIGINT         NOT NULL,
    work_id      BIGINT         NOT NULL,
    paid_price   DECIMAL(10, 2) NOT NULL,
    purchased_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_member_work UNIQUE (member_id, work_id),
    CONSTRAINT fk_purchase_member FOREIGN KEY (member_id)
        REFERENCES member (member_id) ON DELETE RESTRICT,
    CONSTRAINT fk_purchase_work FOREIGN KEY (work_id)
        REFERENCES work (work_id) ON DELETE RESTRICT
);

CREATE INDEX idx_purchase_work_id ON purchase (work_id);
CREATE INDEX idx_purchase_member_id ON purchase (member_id);
