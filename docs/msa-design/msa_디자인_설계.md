# 🛒 E-commerce MSA 디자인 설계

## 📝 개요
본 보고서는 기존 Monolithic 아키텍처로 구성된 E-commerce 시스템을 마이크로서비스 아키텍처(MSA) 기반으로 재설계하기 위한 내용을 담고 있습니다.  
서비스의 복잡성 증가와 유지보수 효율성, 확장성 확보를 위해 도메인 분리를 중심으로 마이크로서비스화하고,  
이를 위한 배포 전략 및 분산 트랜잭션 처리 방식으로 SAGA 패턴을 적용합니다.

---

## 📦 도메인 파악

해당 시스템은 다음의 6개 주요 도메인으로 구성되어 있습니다:

1. **쿠폰 (Coupon)**
2. **주문 (Order)**
3. **결제 (Payment)**
4. **포인트 (Point)**
5. **상품 (Product)**
6. **통계 (Statistics)** – *인기상품 집계*

---

## 🚀 도메인 기반 MSA 배포 전략

각 도메인은 서비스별 독립성을 보장하기 위해 별도 마이크로서비스로 분리하여 배포합니다.  
단, **결제(Payment)와 포인트(Point)** 는 하나의 배포 단위로 구성합니다.

📌 **이유**:  
결제 시 포인트 차감은 필수적인 선행 조건이며, 두 도메인 간 강한 결합이 존재하기 때문입니다.  
두 서비스 간의 네트워크 호출 및 실패 가능성을 줄이고 트랜잭션 안정성을 확보하기 위함입니다.

### ✅ 마이크로서비스 구성

| 도메인         | 서비스 이름                   | 배포 단위       |
|----------------|--------------------------|------------------|
| 쿠폰           | `coupon-service`         | 개별 배포        |
| 주문           | `order-service`          | 개별 배포        |
| 결제 + 포인트  | `payment-charge-service` | **통합 배포**    |
| 상품           | `product-service`        | 개별 배포        |
| 통계           | `statistics-service`     | 개별 배포        |

---

## 🔄 분산 트랜잭션 처리 전략: **SAGA (Choreography 기반)**

### ✅ SAGA 패턴 적용 이유

E-commerce와 같은 복잡한 도메인을 가진 시스템에서는 다양한 서비스 간의 분산 트랜잭션이 자주 발생합니다.  
이러한 분산 환경에서는 **SAGA 패턴**이 더 현실적이고 안정적인 방안입니다.

#### 🌱 왜 SAGA인가?

- **서비스 독립성 유지**
   - 각 서비스는 자신만의 로컬 트랜잭션만 처리하며, 실패 시 보상 로직을 통해 데이터 일관성을 유지합니다.
- **비동기 이벤트 기반 처리**
   - 마이크로서비스 간 직접적인 호출 없이 이벤트를 통해 느슨하게 연결되므로, 서비스 간 결합도가 낮아지고 장애 전파가 줄어듭니다.
- **유연한 장애 복구**
   - 개별 서비스 실패 시 보상 트랜잭션을 통해 전체 흐름을 롤백할 수 있어 회복 가능성이 높습니다.
- **확장성과 성능 확보**
   - 각 서비스는 비동기적으로 처리되므로 전체 시스템의 응답성과 처리량이 향상됩니다.
- **현대적 인프라 친화**
   - Kafka, RabbitMQ, EventBridge 등 이벤트 스트리밍 인프라와 쉽게 통합되어 MSA 환경에서 실용적입니다.

따라서 SAGA 패턴을 적용하며, 특히 **Choreography 기반의 SAGA**를 통해 서비스 간 자율성과 유연성을 극대화합니다.

---

## ⚠️ MSA 환경에서의 트랜잭션 분리 이슈

Monolithic 아키텍처에서는 하나의 데이터베이스 내에서 여러 도메인 간 트랜잭션을 쉽게 묶을 수 있었지만, MSA 환경에서는 **도메인별로 서비스와 데이터베이스가 물리적으로 분리**되기 때문에,  
**단일 트랜잭션으로 묶는 것이 불가능**합니다.

이러한 구조에서 발생하는 핵심 문제는 다음과 같습니다:

- **원자성 보장 어려움**:  
  주문, 결제, 포인트 차감, 재고 감소 등 여러 서비스가 관련된 하나의 **논리적** 트랜잭션을 수행할 때,  
  일부만 성공하고 일부는 실패할 수 있는 **불완전한 상태** 가 발생할 수 있습니다.

- **전통적 트랜잭션 사용 불가**:  
  서비스 간 DB 트랜잭션을 하나의 `Transaction`로 묶는 것은 기술적으로 불가능하며,  
  2PC와 같은 중앙 조정 방식은 MSA 환경에서 성능, 확장성, 안정성 면에서 부적합합니다.

- **장애 복구와 롤백 처리의 복잡성 증가**:  
  서비스 중 하나라도 실패하면 전체 처리를 되돌려야 하는데, 이때 각 서비스는 **자체 보상 로직**을 갖고 있어야 하며,  
  이러한 복구 처리는 명확한 트랜잭션 경계를 제공하지 않으면 매우 어렵고 위험할 수 있습니다.

이러한 이유로 MSA에서는 **트랜잭션을 분산된 서비스 간의 이벤트 흐름으로 대체**하고,  
데이터 정합성을 유지하기 위해 **SAGA 패턴**, 특히 **코레오그래피 기반 SAGA**가 널리 활용됩니다.

### ❌ 오케스트레이션 대신 코레오그래피를 선택한 이유

- 오케스트레이션은 중앙 컨트롤러에 **비즈니스 로직이 집중**되어 **단일 장애 지점(SPOF)**이 발생할 수 있음
- 서비스 간 **결합도가 높아져 독립성과 자율성 훼손**
- 코레오그래피는 각 서비스가 이벤트 기반으로 **자율적으로 반응**하므로 확장성과 유지보수성이 뛰어남

---

### 💳 주문 프로세스 예시
- **SAGA - 코레오그래피 기반**

  1. 사용자가 `order-service`에 주문 요청을 보냄
  2. `order-service`는 존재하는 주문서를 **PENDING → COMPLETED**로 변경한 후, `OrderCompletedEvent` 를 발행함
  3. 이벤트를 수신한 각 서비스는 아래와 같이 작업을 수행함:
      - `coupon-service`: 쿠폰 사용 처리 → 완료 후 아무 이벤트도 발행하지 않음
      - `payment-charge-service`: 결제 금액 차감 수행 → 완료 후 아무 이벤트도 발행하지 않음
      - `product-service`: 재고 차감 수행 → 완료 후 아무 이벤트도 발행하지 않음
  4. 💥 다음 중 하나라도 실패할 경우:
      - 실패한 서비스는 `CouponFailedEvent`, `PaymentFailedEvent`, `InventoryFailedEvent` 중 하나를 발행함
      - `order-service`는 이러한 실패 이벤트를 리스닝하여 주문 상태를 **CANCELED**로 변경하고 `OrderRollbackEvent`를 발행함
  5. `OrderRollbackEvent`를 수신한 각 서비스는 보상 트랜잭션을 수행함:
      - `coupon-service`: 쿠폰 복구
      - `payment-charge-service`: 결제 금액 환불
      - `product-service`: 재고 복구  
        - 🔒 **보상 트랜잭션 수행 조건**
        > 각 서비스는 `OrderRollbackEvent` 수신 시, **해당 요청의 선행 트랜잭션이 실제로 성공했는지 여부를 먼저 확인한 뒤에만 보상 트랜잭션을 실행해야 합니다.**  
             예를 들어, `product-service`가 재고 차감에 실패하여 `InventoryFailedEvent`를 보낸 경우, 해당 서비스는 실제로 재고를 차감하지 않았으므로 재고 복구를 수행하지 않아야 합니다.
          - 주문 프로세스 시퀀스 다이어그램(시나리오 : 재고차감 실패 시 보상트랜잭션) 
          ```mermaid
           sequenceDiagram
           autonumber
           actor  User
           participant OrderService
           participant CouponService
           participant PaymentService
           participant ProductService
        
               User->>OrderService: 주문 요청
               OrderService->>OrderService: 주문 상태 변경 (PENDING → COMPLETED)
               OrderService-->>CouponService: OrderCompletedEvent
               OrderService-->>PaymentService: OrderCompletedEvent
               OrderService-->>ProductService: OrderCompletedEvent
        
               CouponService->>CouponService: 쿠폰 사용 처리
               PaymentService->>PaymentService: 결제 차감 처리
               ProductService->>ProductService: 재고 차감 시도 (실패)
        
               ProductService-->>OrderService: InventoryFailedEvent
        
               OrderService->>OrderService: 주문 상태 변경 (COMPLETED → CANCELED)
               OrderService-->>CouponService: OrderRollbackEvent
               OrderService-->>PaymentService: OrderRollbackEvent
               OrderService-->>ProductService: OrderRollbackEvent
        
               CouponService->>CouponService: 쿠폰 복구
               PaymentService->>PaymentService: 결제 금액 환불
               ProductService->>ProductService: 보상 스킵 (선행 트랜잭션 실패)
           ```


