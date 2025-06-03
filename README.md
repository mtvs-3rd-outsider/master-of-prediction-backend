<div align="center">

<!-- logo -->
<img src="https://user-images.githubusercontent.com/80824750/208554611-f8277015-12e8-48d2-b2cc-d09d67f03c02.png" width="400"/>

### 예측의 달인 ✅

<br/> [<img src="https://img.shields.io/badge/프로젝트 기간-2024.08.01~2024.10.11-green?style=flat&logo=&logoColor=white" />]()

</div> 

다음과 같은 내용을 작성할 수 있습니다.
- 프로젝트 소개
- 프로젝트 구성
- 개요
- 시스템 구성
- 도메인 구조
- 아키텍쳐 특징
- MSA 전환준비
- 보안
- 개발 가이드라인인
- 프로젝트 팀원


## 📝 소개
모든것에 예측게임을 열고 서로 토론하며 배팅하는 커뮤니티형 SNS입니다.

<br />

### 프로젝트 구조
# 프로젝트 아키텍처

## 개요
MSA(Microservices Architecture)를 염두에 두고 설계된 Spring Boot 백엔드 프로젝트입니다. 현재는 모노리스 구조로 구현되어 있지만, 각 도메인이 독립적으로 설계되어 향후 MSA로 전환이 용이합니다.

## 시스템 구성

### 서버 구성
- **메인 서버 (Server 1)**: 비즈니스 로직 처리
- **채팅 서버 (Server 2)**: 실시간 채팅 기능

### 기술 스택
- **인증**: JWT + OAuth
- **메인 서버**: Spring Boot, JPA
- **채팅 서버**: RSocket, WebFlux, R2DBC
- **패턴**: CQRS (검색/구독 기능)
- **스토리지**: MinIO

## 도메인 구조

### 메인 서버 도메인 목록
각 도메인은 Application-Domain-Infrastructure 3계층 구조로 구성됩니다.

1. **Betting** - 베팅 관리
2. **BettingChannelComment** - 베팅 채널 댓글
3. **BettingOrder** - 베팅 주문
4. **CategoryChannelComment** - 카테고리 채널 댓글
5. **CategoryChannel** - 카테고리 채널
6. **ChannelSubscribe** - 채널 구독
7. **Feed** - 피드
8. **Like** - 좋아요
9. **MyChannel** - 내 채널
10. **MyChannelComment** - 내 채널 댓글
11. **Notification** - 알림
12. **QuoteFeed** - 인용 피드
13. **Ranking** - 랭킹
14. **Report** - 신고
15. **User** - 사용자

### 채팅 서버
- **Chat** - 실시간 채팅 (RSocket + WebFlux + R2DBC)

## 아키텍처 특징

### 레이어드 아키텍처
각 도메인은 다음과 같은 3계층 구조로 구성됩니다:

```
Application Layer (API/Controller)
    ↓
Domain Layer (Business Logic)
    ↓
Infrastructure Layer (Data Access/External API)
```

### 도메인 간 통신
도메인 간 통신은 Infrastructure 레이어를 통한 내부 API 호출 방식을 사용합니다.

**주요 통신 예시:**
- BettingOrder → User (사용자 정보 조회)
- Betting → User (베팅 시 사용자 검증)
- Feed → User (피드 작성자 정보)
- MyChannel → ChannelSubscribe (구독 관리)
- QuoteFeed → Feed (원본 피드 참조)
- Notification → User (알림 대상 사용자)

### CQRS 패턴
- **Command**: 구독 기능 (쓰기 최적화)
- **Query**: 검색 기능 (읽기 최적화)

### 데이터 저장소
- **Main Database**: 메인 서버의 모든 도메인 데이터
- **Chat Database**: 채팅 서버의 실시간 데이터 (Reactive)
- **MinIO**: 객체 스토리지 (이미지, 파일)

## MSA 전환 준비사항

### 장점
1. **독립적인 도메인**: 각 도메인이 완전히 분리되어 있음
2. **API 기반 통신**: 도메인 간 HTTP API 호출 방식 사용
3. **독립적인 데이터베이스**: 각 도메인별 데이터 분리 가능
4. **레이어 분리**: Infrastructure 레이어에서 외부 호출 처리

### 전환 시 고려사항
- Infrastructure 레이어의 내부 API 호출을 HTTP 클라이언트로 변경
- 분산 트랜잭션 처리 방안
- 서비스 간 통신 장애 처리 (Circuit Breaker 등)
- 분산 로깅 및 모니터링

## 보안
- **JWT**: 토큰 기반 인증
- **OAuth**: 소셜 로그인 지원

## 개발 가이드라인

### 도메인 개발 원칙
1. 각 도메인은 독립적으로 개발 가능해야 함
2. 다른 도메인과의 통신은 반드시 Infrastructure 레이어를 통해서만 수행
3. 도메인 내부 로직은 Domain 레이어에 구현
4. 외부 API 호출, 데이터베이스 접근은 Infrastructure 레이어에서 처리

### 코드 구조
```
개별서버
├── src/main/java//
│   ├── betting/
│   │   ├── application/     # Controller, Service
│   │   ├── domain/          # Entity, Domain Service
│   │   └── infrastructure/  # Repository, External API Client
│   ├── user/
│   └── ...
└── chat/                    # 채팅 서버 (별도 모듈)
```

이 아키텍처는 현재 요구사항을 충족하면서도 향후 확장성과 유지보수성을 고려한 설계입니다.

<br />

## 💁‍♂️ 프로젝트 팀원
#### 풀스택
<table align="center">
  <tbody>
    <tr>
      <td align="center"><a href="https://github.com/jeonjinhyun"><img src="https://github.com/jeonjinhyun.png" width="100px;" alt=""/><br /><b>전진현</b></a><br /></td>
      <td align="center"><a href="https://github.com/tripleyoung"><img src="https://github.com/tripleyoung.png" width="100px;" alt=""/><br /><b>윤여원</b></a><br /></td>
      <td align="center"><a href="https://github.com/apfp77"><img src="https://github.com/apfp77.png" width="100px;" alt=""/><br /><b>김영규</b></a><br /></td>
    </tr>
    <tr>
      <td align="center">팀장</td>
      <td align="center">팀원</td>
      <td align="center">팀원</td>
    </tr>
  </tbody>
</table>
