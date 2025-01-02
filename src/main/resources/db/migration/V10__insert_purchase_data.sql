
DO
$$
DECLARE
batch_size INT := 25;
    total_size
INT := 100;
    batch_count
INT := total_size / batch_size;
    i
INT;
BEGIN
FOR i IN 1..batch_count LOOP
        INSERT INTO purchase (member_id, work_id, paid_price, purchased_at)
SELECT FLOOR(RANDOM() * (SELECT COUNT(*) FROM member) + 1)::INT AS member_id, wpp.work_id AS work_id,
       wpp.price AS paid_price,
       TIMESTAMP '2024-12-01 00:00:00' +
       (RANDOM() * (EXTRACT(EPOCH FROM TIMESTAMP '2024-12-30 23:59:59' -
                                       TIMESTAMP '2024-12-01 00:00:00')) || ' seconds') ::INTERVAL AS purchased_at
FROM work_pricing_policy wpp
         JOIN generate_series(1, batch_size) g ON TRUE
WHERE TIMESTAMP '2024-12-01 00:00:00' +
      (RANDOM() * (EXTRACT(EPOCH FROM TIMESTAMP '2024-12-30 23:59:59' -
                                      TIMESTAMP '2024-12-01 00:00:00')) || ' seconds')::INTERVAL
            BETWEEN wpp.start_date AND COALESCE(wpp.end_date, 'infinity')
ON CONFLICT (member_id, work_id) DO NOTHING;
END LOOP;
END $$;
