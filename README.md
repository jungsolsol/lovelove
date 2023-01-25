## 러브플레이스
#### 개인프로젝트

### 현재 내위치를 통해 주변 이성을 찾아 보자
------------

## 개발 환경

**언어** 

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=javascript&logoColor=white"> <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white">

**서버** 

<img src="https://img.shields.io/badge/apache tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=white"> <img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"> <img src="https://img.shields.io/badge/ubuntu-FCC624?style=for-the-badge&logo=ubuntu&logoColor=black">

**프레임워크** 

<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/spring data jpa-6DB33F?style=for-the-badge&logo=springdatajpat&logoColor=white"> 



**DB**

<img src="https://img.shields.io/badge/mysql-1572B6?style=for-the-badge&logo=mysql&logoColor=white">  <img src="https://img.shields.io/badge/amazonaws rds-E34F26?style=for-the-badge&logo=amazonawsrds&logoColor=white">

**IDE** 

<img src="https://img.shields.io/badge/intellij-1572B6?style=for-the-badge&logo=intellij&logoColor=white"> 

**API/라이브러리** 

<img src="https://img.shields.io/badge/googlelogin api-DD0031?style=for-the-badge&logo=googlelogin api&logoColor=white"> <img src="https://img.shields.io/badge/kakaomap api-FFCA28?style=for-the-badge&logo=kakaomap api&logoColor=white"> <img src="https://img.shields.io/badge/thymeleaf-47A248?style=for-the-badge&logo=thymeleaf&logoColor=white"> 

**CI/CD** 

<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"> <img src="https://img.shields.io/badge/github action-F05032?style=for-the-badge&logo=github action&logoColor=white"> <img src="https://img.shields.io/badge/codedeploy-4053D6?style=for-the-badge&logo=codedeploy&logoColor=white"> 

**보안** 

<img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> <img src="https://img.shields.io/badge/ssl-000000?style=for-the-badge&logo=ssl&logoColor=white"> 

------------
# 배포

### 배포환경 AWS (비용문제로 배포중지)
#### www.jungsolsol.com

## 프로젝트 아키텍쳐
![image](https://user-images.githubusercontent.com/88434960/206699983-5a77f4b5-07bc-42c2-971e-5bec5ac5ab13.png)


------------

## 구현 

* 백엔드 구현

    * MVC패턴으로 서비스 구현 
    * OAuth2 + Spring Security로 회원가입, 로그인 기능 구현
    * 사용자 정보 CRUD , 매시지 CRUD, 하트 기능 구현
    * Valid를 통한 사용자 프로필 유효성검사 구현 (닉네임(2~10자리), 키(140~220사이), 직업(한글),소개(30자이내))
    * 비즈니스 로직 구현
      * 현재 내위치 주변을 기준으로 주변 이성 Dto 받아오기
      * 하트가 눌려져있을때 , 눌려져있지않을때로 상황을 나누어서 하트를 제거, 추가하는 로직
      * 이성 탐색하는 반경을 조정하는 로직
    * JPQL로 필요한 쿼리 구현
    
* 프론트엔드 구현

    * Vanllia JS + Kakao API (MAP) + geolocation API (현재 위도 경도) + ajax로 백엔드서버와 통신 구현 
    * Thymeleaf로 SSR view 구현 
    * CSS3로 간단한 디자인 구현

------------
# 화면구성 

### 회원가입 

![1-Oauth로그인](https://user-images.githubusercontent.com/88434960/214580708-3716b674-5db2-4970-9a72-82172e43bef2.gif)



### 프로필 작성 
1. 프로필 작성

![ezgif com-gif-maker](https://user-images.githubusercontent.com/88434960/214581993-88f4f132-c2a0-4fd2-9e6a-2e033074ae05.gif)


2. 유효성 검사

![유효성-검사](https://user-images.githubusercontent.com/88434960/214579488-405999c2-01c6-4937-bb38-dd34d24d866f.gif)


### 하트 보내기

![하트-보내기](https://user-images.githubusercontent.com/88434960/214580974-52fb4d51-8ee6-43d8-a40b-50d8511a011e.gif)

