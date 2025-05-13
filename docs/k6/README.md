### K6를 활용한 과부하/성능 테스트 

- setup 
  - brew install k6
  - k6 version
- run 
  - k6 run --summary-trend-stats="min,avg,med,max,p(90),p(95),p(99)" {파일명}.js