## ~~러브플레이스(리팩토링)~~
AWS 요금으로 인한 추후 진행 예정

## 진행중인 리팩토링 사항
1. html markup 문법 개선 요
2. jquery 개선
3. 디자인 개선

## 구현 

* 백엔드 구현

    * MVC패턴으로 서비스 구현 
    * OAuth2 + Spring Security로 회원가입, 로그인 기능 구현
    * 사용자 정보 CRUD , 매시지 CRUD, 하트 기능 구현
    * Valid를 통한 사용자 프로필 유효성검사 구현 (닉네임(2-10자리), 키(140-220사이), 직업(한글),소개(30자이내))
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
