import http from 'k6/http';
import { check } from 'k6';

export const options = {
    vus: 100,           // 100 virtual users
    duration: '30s',     // for 30 seconds
    thresholds: {
        http_req_duration: ['p(99)<100'], // 99% 요청이 200ms 이하
        http_req_failed: ['rate==0'], //   실패율 = 0
        checks: ['rate==1.0'],
    },
};

export default function () {
    const res = http.get('http://localhost:8091/statistic/ranks/top-five');
    check(res, {
        'status is 200': (r) => r.status === 200,
    });
}
