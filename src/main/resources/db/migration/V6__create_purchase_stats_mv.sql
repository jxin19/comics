CREATE
MATERIALIZED VIEW purchase_stats_mv AS
SELECT w.work_id,
       COUNT(*)          as total_purchases,
       SUM(p.paid_price) as total_revenue,
       NOW()             as last_updated_at
FROM work w
LEFT JOIN purchase p ON w.work_id = p.work_id
GROUP BY w.work_id;

CREATE UNIQUE INDEX idx_purchase_stats_mv_work_id
    ON purchase_stats_mv (work_id);


CREATE INDEX idx_purchase_stats_mv_total_purchases
    ON purchase_stats_mv (total_purchases DESC);

-- 갱신 함수
CREATE
OR REPLACE FUNCTION refresh_purchase_stats_mv()
RETURNS void AS $$
BEGIN
    REFRESH
MATERIALIZED VIEW CONCURRENTLY purchase_stats_mv;
END;
$$
LANGUAGE plpgsql;
