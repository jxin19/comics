INSERT INTO work_pricing_policy (
    work_id,
    is_free,
    price,
    start_date,
    end_date
)
SELECT
    w.work_id,
    false as is_free,
    (FLOOR(RANDOM() * 6) + 1) * 500 as price,
    w.created_at as start_date,
    NULL as end_date
FROM work w;


WITH selected_works AS (
    SELECT work_id
    FROM work
    WHERE random() < 0.3
),
work_counts AS (
    SELECT
        work_id,
        floor(random() * 3 + 1)::int as repeat_count
    FROM selected_works
),
expanded_works AS (
    SELECT
        w.work_id,
        generate_series(1, w.repeat_count) as series_num
    FROM work_counts w
),
date_ranges AS (
    SELECT
        work_id,
        series_num,
        TIMESTAMP '2024-10-01 00:00:00' +
            (random() * (TIMESTAMP '2025-01-01 00:00:00' -
                        TIMESTAMP '2024-10-01 00:00:00'))::interval as start_date,
        TIMESTAMP '2024-11-01 00:00:00' +
            (random() * (TIMESTAMP '2025-02-01 00:00:00' -
                        TIMESTAMP '2024-11-01 00:00:00'))::interval as end_date
    FROM expanded_works
)
INSERT INTO work_pricing_policy (
    work_id,
    is_free,
    price,
    start_date,
    end_date
)
SELECT
    work_id,
    true as is_free,
    0 as price,
    start_date,
    end_date
FROM date_ranges dr
WHERE end_date > start_date
  AND start_date >= TIMESTAMP '2024-10-01 00:00:00'
  AND end_date <= TIMESTAMP '2025-02-01 00:00:00'
ORDER BY start_date;
