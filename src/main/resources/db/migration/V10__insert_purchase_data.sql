DO $$
DECLARE
batch_size INT := 5000;
    total_size INT := 100000;
    batch_count INT := total_size / batch_size;
    dummy_start_date TIMESTAMP WITH TIME ZONE := '2024-12-01 00:00:00+00';
    dummy_end_date TIMESTAMP WITH TIME ZONE := '2024-12-30 23:59:59+00';
    i INT;
BEGIN
FOR i IN 1..batch_count LOOP
        WITH random_member_work AS (
            SELECT
                m.member_id,
                wpp.work_id,
                wpp.price as paid_price
            FROM member m
            CROSS JOIN LATERAL (
                SELECT w.work_id, wpp.price
                FROM work w
                JOIN work_pricing_policy wpp ON w.work_id = wpp.work_id
                WHERE NOT wpp.is_free
                AND wpp.start_date <= now()
                AND (wpp.end_date IS NULL OR wpp.end_date >= now())
                ORDER BY RANDOM()
                LIMIT 1
            ) wpp
            ORDER BY RANDOM()
            LIMIT batch_size
        )
        INSERT INTO purchase (
            member_id,
            work_id,
            paid_price,
            purchased_at
        )
SELECT
    member_id,
    work_id,
    paid_price,
    dummy_start_date + (random() * (dummy_end_date - dummy_start_date))
FROM random_member_work
    ON CONFLICT (member_id, work_id) DO NOTHING;
END LOOP;
END $$;
