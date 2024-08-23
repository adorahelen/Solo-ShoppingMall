# 프로젝트 구성 
<img width="700" alt="image" src="https://github.com/user-attachments/assets/1f1d631e-959d-4fa8-8213-7bbfbd6d08dc">

- RESTful API 서버 구현스프링 부트와 JPA를 이용하는 API 서버 만들기 
- SpringBoot : @RestController에서 모든 응답처리, @RestControllerAdvice에서 모든 예외처리
- SpringDataJPA : 목록데이터는 Querydsl로 설계하고, Projections를 이용해서 DTO로 추출한다.
- SpringSecurity : JWT의 AccessToken과 RefreshToken, 동시에 SecurityContext 활용
  


## REST API 
<img width="700" alt="image" src="https://github.com/user-attachments/assets/5502bdde-a36e-4ed3-9a8d-fbdea00cbb8f">

- 기존 브라우저는 단순한 뷰어 역할만을 수행, 반면 Web 2.0부터 프로그램이 실행되는 하나의 플랫폼으로 자리잡음
    * 반응형 웹은 CSS 기법, 모바일 앱 등은 데이터만을 요구 => 컴퓨터 일편적인 HTML View ( X )
    * Android app, IOS app, Browser, Applications, etc 등에게 동일한 데이터를 보내는게 API서버
- 서버에서 완성본을 보내주는 방식에서 -> 순수한 데이터를 보내주는 방식으로 변화 (배달음식->밀키트)
- REST 방식 혹은 Restfull 은 Http/Htpps를 이용해서 원하는 데이터를 제공하는 방식에 사용하는 구조 의미
    * 이 REST는 정해진 스펙이 존재하지 않음, 단지 조건이나 가이드만 존재  

## 쿼리스트링 (Query String) 
- @RequestParam(?로 시작하는 쿼리스트링) vs @PathVariable(고정된 주소)
- https://github.com/adorahelen?tab=overview&from=2024-08-01&to=2024-08-23 
- https://career.programmers.co.kr/competitions (고유한 식별자 사용) 
- 페이지 번호와 같이, 오늘 1페이지의 내용이 시간이 지나면 변경될 수 있기에, 식별자X -> 쿼리스트링O
    * 매번 동일한 데이터를 나타내지 않는다면 쿼리스트링으로 처리된다. 
