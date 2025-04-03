## 상품 기본정보 조회 (재고,옵션 포함)

```mermaid
sequenceDiagram
    autonumber
    actor Client as Client
    participant API as API
    participant Product as Product
    
    Client  ->> API : 상품 기본정보 요청
    API ->> Product : 상품 기본정보 조회
    activate API
    activate Product
    Product ->> API : 상품 기본정보 반환
    
    deactivate Product
    API ->> Client : 200 OK - 상품 기본정보 반환
    deactivate API
```

 
