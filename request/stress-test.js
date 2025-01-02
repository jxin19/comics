import http from 'k6/http';
import { sleep, check } from 'k6';

export let options = {
    stages: [
        { duration: '30s', target: 3000 },
    ],
};

export default function () {
    const workId =  Math.floor(Math.random() * (9 - 3 + 1)) + 3;

    // API 호출
    let res = http.get(`http://localhost:8080/work/${workId}`, {
        headers: {
            'Accept': 'application/json',
            'Authorization': 'Bearer eyJhbGciOiJIUzM4NCJ9.eyJtZW1iZXJJZCI6MiwiZW1haWxBZGRyZXNzIjoidGVzdDJAdGVzdC5jb20iLCJpc0FkdWx0VmVyaWZpZWQiOmZhbHNlLCJpYXQiOjE3MzU4MDQ0MDAsImV4cCI6MTczNjEwNDQwMH0.LGbdLpDJnee2pEIv0E2lZIPr79hoJXRjugKgZcHEEGTKO4KwHNpBJ5TrHIbcwtik',
        },
    });

    // 응답 확인
    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    // 요청 간격 (1초)
    sleep(1);
}
