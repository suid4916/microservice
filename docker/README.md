# Keycloak 서버 추가

시작하기에 앞서(로컬에서 도커로 keycloak을 수행하는 경우)

- Windows의 경우

    C:\Windows\system32\drivers\etc\host
    
위 경로에

    127.0.0.1 keycloak
    
을 추가

- Linux의 경우

    /etc/hosts
    
위 경로에

    127.0.0.1 keycloak
    
을 추가

    Docker는 'Container name'으로 통신하지만(ex http://configserver:8080/actuator), 
    Post method로 URL을 호출하는 Postman의 경우 'localhost'로 호출하기 때문(ex http://localhost:8080/actuator).

위 작업을 하지 않는 경우, jwt(JSON Web Token)에서 iss(issure)의 발급 대상은 localhost로 표기되고, 서비스에서 토큰을 입력하고 Send하면 keycloak이 발급한 대상으로 표기 되기 때문에 401에러가 출력된다.

Keycloak의 realm, client, role 등의 방법은 하단의 링크를 참조한다.

* https://www.baeldung.com/spring-boot-keycloak


'Keycloak adapter'는 2022년으로 deprecated 되었기 때문에 'spring oauth2 client'를 사용 함

* https://www.keycloak.org/2022/02/adapter-deprecation
