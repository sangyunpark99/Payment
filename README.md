# 💰 Payment 💰

[![DBMS](https://img.shields.io/badge/DBMS-MySQL-orange)](https://www.mysql.com/downloads/)

## 📖 Description

100만 유저가 사용하는 대용량 트래픽에 대응가능한 핀테크 서버를 구현하고,

발생하는 문제들에 대해 직접 해결하고자 이 프로젝트를 시작했습니다.


## 🚀 Feature
### 회원 가입 및 로그인
- 회원 생성 API
- 회원정보 수정 API
- 회원 삭제 API
### 계좌
- 계좌 생성 API
- 계좌 이체 API
- 계좌 조회 API
- 계좌 잔액 확인 API
- 계좌 해지 API
    
### 예금
- 예금 API

### 결제
- Kotlin


## 💻 ERD


<img width="865" alt="image" src="https://github.com/user-attachments/assets/544497c9-9235-4ab7-b6ea-c031d723ccd1">




## 💥 Trouble Shooting
### 1. 당행 이체시 발생할 수 있는 동시성 문제
Double Spend Issue: 두 개의 트랜잭션이 동시에 동일한 계좌에서 동일한 금액을 이체하려고 할 때, 충분한 잔액이 없는 상황에서 각각의 트랜잭션이 잔액을 충분하다고 판단 함으로 인해 계좌 잔액이 부족한데도 이체가 완료되는 상황


[✅ Double Spend Issue 문제 해결 방법](https://sangyunpark99.tistory.com/17)


### 2. 이벤트로 인해 발생할 수 있는 서버 부하 문제
신규 가입 이벤트로 인해서 동시에 만명의 유저가 가입 요청을 하게되는 경우, 급격한 트래픽 증가로 인해 서버 시스템에 과부하가 오는 상황

[트래픽 부하 문제 해결](https://sangyunpark99.tistory.com/entry/%ED%9A%8C%EC%9B%90-%EA%B0%80%EC%9E%85-%EC%9D%B4%EB%B2%A4%ED%8A%B8%EB%A1%9C-%EC%9D%B8%ED%95%B4-%EC%83%9D%EA%B8%B0%EB%8A%94-%ED%8A%B8%EB%9E%98%ED%94%BD-%EC%B2%98%EB%A6%AC%ED%95%98%EA%B8%B0feat-%ED%8A%B8%EB%9E%98%ED%94%BD-%EB%8B%A4%EB%A4%84%EB%B3%B4%EA%B8%B0)


### 3. 이슈를 완료할 때마다 일일히 AWS EC2에 배포해야 하는 문제


## ✍🏻 블로그
https://sangyunpark99.tistory.com/
