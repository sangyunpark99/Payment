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
- 게좌 생성 API
- 계좌 삭제 API
- 계좌 이체 API



## 💻 ERD


<img width="865" alt="image" src="https://github.com/user-attachments/assets/544497c9-9235-4ab7-b6ea-c031d723ccd1">




## 💥 Trouble Shooting
### 당행 이체시 발생할 수 있는 동시성 문제
1. Double Spend Issue: 두 개의 트랜잭션이 동시에 동일한 계좌에서 동일한 금액을 이체하려고 할 때, 충분한 잔액이 없는 상황에서 각각의 트랜잭션이 잔액을 충분하다고 판단 함으로 인해 계좌 잔액이 부족한데도 이체가 완료되는 상황

2. Deadlock: 두 개 이상의 트랜잭션이 서로 다른 자원에 대해 잠금을 요청하고, 각 트랜잭션이 다른 트랜잭션이 소유한 잠금을 필요로 하는 상황이 발생한다. 이로 인해 트랜잭션이 서로 대기 상태에 빠져 무한 대기하게 되는 데드락이 발생하는 상황


3. Lost Update: 두 개 이상의 트랜잭션이 동시에 동일한 자원의 값을 읽고, 각각의 트랜잭션이 읽은 값을 기반으로 자원의 값을 수정할 때 발생한다. 첫 번째 트랜잭션이 업데이트한 값이 두 번째 트랜잭션에 의해 덮어쓰여지는 문제가 발생하는 상황


4. Dirty Read: 하나의 트랜잭션이 아직 커밋되지 않은 다른 트랜잭션의 변경 사항을 읽는 경우 발생한다. 만약 첫 번째 트랜잭션이 롤백되면 두 번째 트랜잭션은 유효하지 않은 데이터를 기반으로 작업을 수행하게 됩니다.



## ✍🏻 블로그
https://sangyunpark99.tistory.com/
