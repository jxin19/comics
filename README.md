# Comics Service API

## 1. 프로젝트 목적
이 프로젝트는 웹소설/웹툰 플랫폼의 백엔드 API를 구현합니다. 사용자들이 작품을 조회하고 구매할 수 있으며, 인기 작품을 확인하고 작품 관련 데이터를 관리할 수 있는 기능을 제공합니다.

주요 기능:
1. 작품 조회 이력 관리
2. 인기 작품 분석 (조회수/구매 기준)
3. 작품 구매 시스템
4. 이벤트 기간 작품 가격 정책 관리
5. 작품 및 관련 데이터 관리

## 2. 기술 스펙

- 언어: Kotlin 1.9.25
- 프레임워크: Spring Boot 3.3.5
- 빌드 도구: Gradle 8.10.2
- 데이터베이스:
    - PostgreSQL: 메인 데이터베이스
    - MongoDB: 로그 데이터 저장
    - Redis: 캐시
- 메시지 큐: Apache Kafka
- ORM: Spring Data JPA, Spring Data MongoDB
- API 문서화: Swagger (SpringDoc OpenAPI)
- 테스트: JUnit 5, Mockito
- 컨테이너: Docker(Docker Compose)

## 3. 프로젝트 구조

```
src
├── main
│   ├── avro # Avro 스키마 파일
│   ├── kotlin
│   │   └── com.homework.comics
│   │       ├── ComicsApplication.kt
│   │       ├── common
│   │       │   ├── aop
│   │       │   ├── application
│   │       │   ├── authentication
│   │       │   ├── component
│   │       │   ├── config
│   │       │   ├── domain
│   │       │   ├── dto
│   │       │   ├── exception
│   │       │   └── handler
│   │       ├── member
│   │       │   ├── application
│   │       │   ├── domain
│   │       │   ├── repository
│   │       │   └── ui
│   │       ├── purchase
│   │       │   ├── application
│   │       │   ├── domain
│   │       │   ├── repository
│   │       │   └── ui
│   │       ├── purchaseStats
│   │       │   ├── application
│   │       │   ├── domain
│   │       │   ├── repository
│   │       │   ├── scheduler
│   │       │   └── ui
│   │       ├── viewLog
│   │       │   ├── application
│   │       │   ├── domain
│   │       │   ├── repository
│   │       │   └── ui
│   │       ├── viewStats
│   │       │   ├── application
│   │       │   ├── domain
│   │       │   ├── repository
│   │       │   ├── scheduler
│   │       │   └── ui
│   │       ├── work
│   │       │   ├── application
│   │       │   ├── domain
│   │       │   ├── repository
│   │       │   └── ui
│   │       └── workPricingPolicy
│   │           ├── application
│   │           ├── domain
│   │           ├── repository
│   │           └── ui
│   └── resources
│       ├── db/migration # Flyway 마이그레이션 스크립트
│       └── application.yml # 애플리케이션 설정 파일
└── test
    └── kotlin
        └── com.homework.comics
            ├── common
            ├── member
            ├── purchase
            ├── viewLog
            ├── viewStats
            ├── work
            └── workPricingPolicy
```

### 패키지 구조 설명

- `common`: 공통 유틸리티, 설정, 예외 처리 등
- `member`: 회원 관련 기능
- `purchase`: 작품 구매 관련 기능
- `purchaseStats`: 구매 통계 관련 기능
- `viewLog`: 작품 조회 이력 관련 기능
- `viewStats`: 조회 통계 관련 기능
- `work`: 작품 관련 기능
- `workPricingPolicy`: 작품 가격 정책 관련 기능

각 도메인 패키지는 다음과 같은 레이어로 구성됩니다:
- `application`: 비즈니스 로직, 서비스 레이어
- `domain`: 도메인 모델, 엔티티
- `repository`: 데이터 접근 레이어
- `ui`: 컨트롤러, Request/Response DTO
- `scheduler`: 스케줄러


## 4. API 엔드포인트
`*` 표시된 API는 JWT 토큰이 필요합니다.

### 회원 관리
[http파일 보기](./request/member.http)
- POST /member: 회원 가입
- PUT /member: 회원 정보 수정`*`
- PUT /member/verify-adult/{identityCheckValue}: 성인 인증`*`
- POST /member/login: 로그인`*`
- POST /member/refresh-token: 토큰 재발급`*`
- POST /member/logout: 로그아웃`*`
- GET /member: 회원 상세 조회`*`

### 작품
[http파일 보기](./request/work.http)
- GET /work/{id}: 작품 상세 조회`*`
- POST /work: 작품 생성
- PUT /work/{id}: 작품 수정
- DELETE /work/{id}: 작품 삭제

### 작품 가격 정책
[http파일 보기](request/work-pricing-policy.http)
- GET /work-pricing-policy/work/{workId}: 작품의 현재 적용 가격 정책 조회
- POST /work-pricing-policy: 가격 정책 생성
- PUT /work-pricing-policy/{id}: 가격 정책 수정
- DELETE /work-pricing-policy/{id}: 가격 정책 삭제

### 구매
[http파일 보기](./request/purchase.http)
- GET /purchase/{id}: 구매 내역 상세 조회
- GET /purchase/member/{memberId}: 회원별 구매 내역 조회
- GET /purchase/work/{workId}: 작품별 구매 내역 조회
- POST /purchase/work/{workId}: 작품 구매`*`

### 조회 이력
[http파일 보기](./request/view-log.http)
- GET /view-log: 작품별 조회 이력 조회
    - 필터링 옵션: workId, memberId, startDate, endDate
    - 페이징 옵션: page, size

### 구매 통계
[http파일 보기](./request/purchase-stats.http)
- GET /purchase-stats/top-works: 인기 구매 작품 조회 (상위 N개)

### 조회 통계
[http파일 보기](./request/view-stats.http)
- GET /view-stats/top-viewed: 조회수 기준 인기 작품 목록 조회


## 5. 주요 코드 설명

### 대용량 트래픽 처리
- Redis를 활용한 캐시 레이어 구현(`WorkQueryServiceImpl`참고)
  ```
    @Cacheable(cacheNames = ["work"], key = "#id + '::' + @authenticationFacade.isAdultVerified()")
    override fun findById(id: Long): WorkServiceResponse {
  ```
- MongoDB를 활용한 로그 데이터 효율적 처리
- Kafka를 활용한 비동기 이벤트 처리(`ViewLogCommandServiceImpl` 참고)
    - 조회이력 메시지 프로듀싱
      ```
        override fun create(workId: Long, memberId: Long) {
          kafkaTemplate.send(
              KafkaConfig.MESSAGE_TOPIC,
              workId.toString(),
              ViewAvro(workId, memberId, Instant.now())
          )
        }
      ```
    - 조회이력 메시지 컨슈밍
      ```
        @KafkaListener(
            topics = [KafkaConfig.MESSAGE_TOPIC],
            groupId = KafkaConfig.MESSAGE_CONSUMER_GROUP,
        )
        @Retryable(
            value = [Exception::class],
            maxAttempts = KafkaConfig.MAX_RETRY_ATTEMPTS,
            backoff = Backoff(delay = 1000, multiplier = 2.0, maxDelay = 6000)
        )
        fun consumeMessage(newMessages: List<ConsumerRecord<String, ViewAvro>>, acknowledgment: Acknowledgment) {
            val viewLogs = newMessages.map {
                val viewAvro = it.value()
                ViewLog(
                    workId = viewAvro.workId,
                    memberId = viewAvro.memberId,
                    viewedAt = viewAvro.viewedAt
                )
            }

            viewLogRepository.bulkInsert(viewLogs)

            acknowledgment.acknowledge()
        }
      ```

### 작품 조회 데이터 처리 파이프라인
- 데이터 처리
  - PostgreSQL의 기본 테이블에 구매 데이터 저장
- 통계 데이터 집계
  - 스케줄러를 활용하여 주기적으로 Materialized View 갱신
  - 인기 작품 미리 집계된 데이터 관리
  - 복잡한 집계 쿼리의 실행 비용 절감
- 다층적 캐싱 전략
  - Materialized View의 데이터를 Redis에 캐싱
  - Redis TTL 설정으로 데이터 신선도 유지
  - 캐시 -> Materialized View -> 원본 테이블 순의 계층화된 조회 구조
  - DB 부하 분산 및 응답 시간 최적화

### 보안
- JWT 기반 인증
  - JWT 리프레시 토큰을 관리하는 서비스(`JwtTokenService` 참고)
  - JWT 리프레시 토큰을 Redis에 보관
  - JWT 토큰을 생성하고 검증하는 기능(`JwtProvider` 참고)
  - REST 호출 시 JWT 인증을 처리(`AuthenticationFilter` 참고)
- Spring Security를 통한 인가 처리(`SecurityConfig` 참고)
- 성인 작품 접근 제어(`WorkQueryServiceImpl`참고)
  ```
    if (work.isAdultOnly && !authenticationFacade.isAdultVerified()) {
        throw AdultOnlyAccessDeniedException("성인 인증이 필요한 작품입니다.")
    }
  ```

### 데이터베이스 설계
- PostgreSQL: 작품, 사용자, 구매 정보 등 핵심 데이터
- MongoDB: 조회 이력, 통계 데이터
- Redis: 인기 작품 순위, 세션 정보

### DB 마이그레이션
- `Flyway`로 변경 이력을 관리합니다.
- 충돌없이 DB 변경사항을 공유할 수 있습니다.

### 확장성
- 도메인 주도 설계 원칙 적용
- 인터페이스 분리 원칙 (ISP) 준수(`WorkPricingPolicyRepository` 참고)


## 실행 방법

1. 프로젝트를 클론합니다:
   ```
   git clone git@github.com:jxin19/comics.git
   cd [project-directory]
   ```

2. Docker Compose를 사용하여 필요한 서비스를 실행합니다:
   ```bash
   docker-compose up -d
   ```
    - PostgreSQL: localhost:5432
    - MongoDB: localhost:27017
    - Redis: localhost:6378
    - Kafka: localhost:9092
    - Kafka UI: localhost:9091
    - Schema Registry: localhost:8081

3. API 애플리케이션을 빌드하고 실행합니다:

   `./gradlew bootRun`
    - 애플리케이션은 http://localhost:8080 에서 실행됩니다.


### 참고
- 실행된 API 애플리케이션(컨테이너)은 http://localhost:8080 입니다.
- 최초 실행 시, 더미데이터를 생성하게 됩니다.
- 다음 3번 절차는 Docker를 실행하지 않고 애플리케이션을 실행하는 절차입니다.


## 기능 테스트 실행

`./gradlew test`


## 부하 테스트 실행

1. k6 설치
   `brew install k6`
2. k6 설치 확인
   `k6 version`
3. 스크립트 실행
   `k6 run request/stress-test.js`

## API 문서
애플리케이션 실행 후 http://localhost:8080/swagger-ui.html 에서 Swagger UI를 통해 API 문서를 확인할 수 있습니다.
