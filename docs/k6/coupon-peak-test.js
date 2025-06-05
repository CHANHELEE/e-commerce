import http from 'k6/http';
import { check } from 'k6';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.4.0/index.js';

export const options = {
    scenarios: {
        spike_then_cooldown: {
            executor: 'ramping-arrival-rate',
            startRate: 0,
            timeUnit: '1s',
            preAllocatedVUs: 4000,
            maxVUs: 4000,
            stages: [
                { target: 1000, duration: '10s' },
                { target: 4000, duration: '10s' }, // 해당 target 을 점진적으로 증가시켜 테스트
                { target: 30, duration: '37s' },
                { target: 3, duration: '10s' },
            ],
        },
    },
    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)< 500'],
    },
};

export default function () {
    const url = 'http://localhost:8091/coupons';
    const payload = JSON.stringify({
        userId: randomIntBetween(1, 10000),
        couponId: randomIntBetween(1, 4),
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(url, payload, params);

    check(res, {
        'status is 200': (r) => r.status === 200,
    });
}
