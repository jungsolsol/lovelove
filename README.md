## 러브플레이스
#### 개인프로젝트

### 현재 내위치를 통해 주변 이성을 찾아 보자
------------

## 개발 환경

**언어** Java(JDK 11), HTML/CSS, Javascript

**서버** Apache Tomcat , AWS EC2, ubuntu

**프레임워크** SpringBoot , SpringJPA

**DB** Mysql, AWS RDS

**IDE** IntelliJ

**API/라이브러리** GoogleLogin API, KakaoMap API, Thymeleaf

**CI/CD** Git, GitAction, CodeDeploy 

**보안** SpringSecurity, SSL

------------
# 배포

## 배포환경 AWS (비용문제로 배포중지)
## www.jungsolsol.com

## 프로젝트 아키텍쳐
![image](https://user-images.githubusercontent.com/88434960/206699983-5a77f4b5-07bc-42c2-971e-5bec5ac5ab13.png)


------------

## 구현 

* 백엔드 구현

    * MVC패턴으로 서비스 구현 
    * OAuth2 + Spring Security로 회원가입, 로그인 기능 구현
    * 사용자 정보 CRUD , 매시지 CRUD, 하트 기능 구현
    * 비즈니스 로직 구현
      * 현재 내위치 주변을 기준으로 주변 이성 Dto 받아오기
      * 하트가 눌려져있을때 , 눌려져있지않을때로 상황을 나누어서 하트를 제거, 추가하는 로직
      * 이성 탐색하는 반경을 조정하는 로직
    * JPQL로 필요한 쿼리 구현

* 프론트엔드 구현

    * Vanllia JS + Kakao API (MAP) + geolocation API (현재 위도 경도) + ajax로 백엔드서버와 통신 구현 
    * Thymeleaf로 SSR view 구현 
    * CSS3로 간단한 디자인 구현


