# 송금 견적 API 개발 프로젝트

## 목차

1. [개요](#개요)
2. [주요 기능](#주요-기능)
3. [기술 스택](#기술-스택)
4. [프로젝트 회고록](#프로젝트-회고록)
   - [JWT 인증/인가 구현](#1-jwt-인증인가-구현)
   - [WebClient 외부 통신](#2-webclient-외부-통신)
   - [주요 기능 구현 리뷰](#3-주요-기능-구현-리뷰)
5. [API 문서](#api-문서)
6. [환경 설정](#환경-설정)
7. [실행 방법](#실행-방법)
8. [테스트 실행](#테스트-실행)
9. [주의사항](#주의사항)
10. [라이센스](#라이센스)

---

## 개요

송금 견적서 생성 및 관리를 위한 REST API 서비스입니다.

## 주요 기능

- 회원 관리 (회원가입/로그인)
- 송금 견적서 생성
- 송금 견적서 요청
- 송금 견적 내역 조회

## 기술 스택

- **Java 17**
- **Spring Boot 3.2**
- **Spring Security + JWT**
- **WebClient**
- **JPA**
- **H2DB**

## 프로젝트 회고록

### 1. JWT 인증/인가 구현

#### 구현 방법

- Spring Security + JWT 토큰 방식 채택
- AccessToken만 사용하여 구현 (RefreshToken은 미적용)
- SecurityFilterChain에서 JWT 검증 필터 추가



### 2. WebClient 외부 통신

#### 구현 방법

- RestTemplate 대신 WebClient 선택 (비동기 처리 장점)
- ConnectionTimeout, ReadTimeout 설정
- application/json 헤더 설정


### 3. 주요 기능 구현 리뷰

#### 회원가입/로그인

##### 구현 내용

- 이메일 기반 회원가입
- 비밀번호 암호화 (BCrypt)
- 사업자 번호 및 주민 번호 암호화 (AES)
- 로그인 시 JWT 토큰 발급

##### 개선점

- 이메일 인증 프로세스 추가 필요
- 사업자 번호 또는 주민번호 복호화 함수 구현
- 소셜 로그인 확장성 고려
- 비밀번호 정책 강화

#### 송금 견적서 생성

##### 구현 내용

- 환율 정보 기반 송금액 계산
- 수수료 정책 적용
- 견적서 유효기간 설정

##### 검증 결과

- 환율 계산 정밀도 이슈 발견 및 수정
   - BigDecimal 사용으로 정밀도 향상
   - 반올림 정책 명확화
- 수수료 계산 로직 검증 완료

#### 송금 견적서 요청/확인

##### 구현 내용

- 일일 송금한도 검증
- 견적서 상태 관리
- 송금 이력 조회

##### 개선점

- 대용량 트래픽 고려한 캐싱 전략 필요
- 이력 조회 성능 최적화
- 페이징 처리 도입

## API 문서

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 환경 설정

### 필수 요구사항

- Java 17+
- SpringBoot 3.2

## 실행 방법

```bash
./gradlew bootRun
```

## 테스트 실행

```bash
./gradlew test
```

## 주의사항
- 환율 정보는 실시간 외부 API와 연동됩니다.
- 모든 금액 계산은 BigDecimal을 사용하여 정밀도를 보장합니다.
- JWT 토큰의 기본 만료시간은 30분입니다.
