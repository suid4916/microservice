# microservice 
## 개발 환경

| Name | Version |
|------------------------|-----|
| Java | 1.8 |
| Spring boot | 2.7.9 |
| MySQL | 8.0.32 |
| Docker for Desktop | 4.17.1 |

--------------------------------

## 주의사항
보안의 Keycloak의 경우, Keycloak-adapter가 2022.02로 deprecated 되었기 때문에 독자적으로 기능 구현한 부분이 있음.

--------------------------------

## 스프링 부트 어플리케이션 적용 내용(microservice_licensing, microservice_organization)

1. `HttpMethod(GET, POST, PUT, DELETE)`를 활용한 서비스 구축
2. `ResourceBundleMessageSource`를 활용한 서비스의 국제화
3. `HATEOAS`를 적용하여 API 가이드 지원
4. `actuator`를 활용한 서비스 인스턴스 체크

---------------------------------

## Docker (docker & 각 폴더 하위의 Dockerfile)

1. Maven의 도커 플러그인을 적용   
` > mvnw clean package dockerfile:build`
2. 도커 이미지로 컨테이너 실행     

| 명령어 | 설명 |
|---------------------------------------------------|--------------------------|
| ` > docker run ${REPOSITORY}:${TAG}` | 도커 이미지 실행 |
| ` > docker run -d ${REPOSITORY}:${TAG}` | 백그라운드 실행 |
| ` > docker run -it -p8080:8080 ${REPOSITORY}:${TAG}` | 도커 내부외 로컬 간의 통신 포트 설정 |
| ` > docker ps`  | 컨테이너 리스트 출력 |
| ` > docker stop ${container_id}` | 컨테이너 중지 |

3. docker-compose 실행   
`> docker-compose -f docker/docker-compose.yml up -d` (-d는 백그라운드 실행)

----------------------------------

## 스프링 클라우드 컨피그 서버 (configserver)

1. 스프링 클라우드 버전 `2021.0.6` 을 사용
2. 해당 버전은 기본 설정을 자동으로 하지 않기 때문에 Maven 에 `spring-cloud-starter-bootstrap` 패키지를 spring cloud를 사용하는 모든 어플리케이션에 추가
3. configserver의 메인에 `@EnableConfigServer` 어노테이션 추가
4. 각 서비스에 jpa 적용
- com.spring.microservice.model.`License`  
- com.spring.microservice.repository.`LicenseRepository`    
- com.spring.microservice.service.`LicenseService`   
5. 서비스의 properties에 import 옵션 추가     
`spring.config.import=optional:configserver:`      
`spring.cloud.config.uri=http://localhost:8071`      
6. Config 구성방법은 `native`, `git`, `vault`가 있음    
_configserver 폴더 내부의 properties 파일 참조_   
- `native` : `(native)application.properties`
- `git` : `(git_and_native)application.properties`
- `vault` : `(vault)application.properties`

* vault 서버 생성 및 설정      
1. docker로 vault 서버 import 및 실행     
`docker run -d -p 8200:8200 --name vaule -e VAULT_DEV_ROOT_TOKEN_ID=myroot -e VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200 vault`     
*-e 변수에 ' 를 쓸경우 잘못 인식됨.* (~~-e 'VAULT_DEV_ROOT_TOKEN_ID=myroot'~~)    
2. `http://localhost:8200/ui/vault/auth` 로 로그인      
*`Method`는 `Token`, `Token`은 상단의 `VAULT_DEV_ROOT_TOKEN_ID의 값`*      
3. 신규 시크릿 엔진 설정 및 값 생성
>  1. `Enable New Engine`으로 새 엔진 생성   
>  2. `KV Engine`으로 선택, 다음    
>  3. `path`는 `licensing-service`, `version`은 `2`
>  4. `create secret`으로 신규 시크릿 생성
>  5. `Path for this secret`에 `licensing-service`, `Secret data`에 Key, Value 입력 후 save (ex)각각 `license.vault.property`, `Welcome to Valut` 입력    
4. Postman에서 (GET) `http://localhost:8071/licensing-service/default` 로 `Header`에 `Key`, `value`를 각각 `X-Config-Token`, `myroot` 입력 후 호출
5. 속성의 암호화      
`bootstrap.properties` 파일 생성 후 파일 안에 `encrypt.key` 설정      
(application.properties에 설정하면 Key 값이 쉬운 문자로 구성되어 있다는 오류와 함께 실행이 불가능하다.)     
  **물론 이는 로컬 테스트용이기 때문에 파일에 직접 설정한 것이며, 일반적으로 Dockerfile의 OS 환경 변수로 참조한다고 한다**     
6. (POST) `http://localhost:8071/encrypt` 에 `raw` 값으로 암호화 할려는 정보를 입력 후 Send
7. 암호화한 데이터앞에 `{cipher}` 키워드 추가로 적용


----------------------------------------------

# 유레카 서버 구축 (eureka)

1. 스프링 클라우드 버전 `2021.0.6` 을 사용
2. 해당 버전은 기본 설정을 자동으로 하지 않기 때문에 Maven 에 `spring-cloud-starter-bootstrap` 패키지를 spring cloud를 사용하는 모든 어플리케이션에 추가
3. `application.properties`에 configserver 정보 추가
4. `configserver` 내부에 `eureka-server.properties` 추가
5. `main` 클래스에 `@EnableEurekaServer` 어노테이션 추가
6. 각 서비스에 `spring-cloud-starter-netflix-eureka-client` 패키지 추가
7. `http://localhost:8070` 에 접속하여 `instance 등록 여부 확인`       

### 유레카를 통한 서비스 검색     
- 스프링 Discovery Client
- REST 템플릿을 사용한 스프링 Discovery Client
- 넷플릭스 Feign 클라이언트

1. Client 타입 별로 호출할 수 있는 엔드포인트 추가     
com.spring.microservice.controller.`LicenseController` 의 `getLicensesWithClient` 함수 참조    
2. DB에서 조회할 수 있는 서비스  추가     
com.spring.microservice.service.`LicenseService` 의 `getLicense` 함수 참조    
3. `http://<IP>:<PORT>/v1/organizaation/<organizationID>/license/<licenseID>/<client type(feign, discovery, rest)>` 로 조회 가능     

###### 서비스 인스턴스 검색 방법 3가지
> 1. DiscoveryClient로 서비스 인스턴스 검색     
> `main`에 `@EnableDiscoveryClient` 어노테이션 추가 및 `DiscoveryClient` 컴포넌트 클래스 생성(`com.spring.microservice.service.client.OrganizationDiscoveryClient` 참조)     
> 2. RestTemplate으로 서비스 검색       
> `main`에 `getRestTemplate` 함수 추가 및 `RestTemplateClient` 컴포넌트 클래스 생성(`com.spring.microservice.service.client.OrganizationRestTemplateClient` 참조)     
> 3. Feign Client로 서비스 호출      
> `main`에 `@EnableFeignClients` 어노테니션 추가 및 `FeignClient` 컴포넌트 인터페이스 생성(`com.spring.microservice.service.client.OrganizationFeignClient` 참조)      



-------------------------------------

# Resilience4j 로 회복성 적용 (microservice_licensing)

##### Resilience4j에서 사용하는 패턴
| 패턴 | 설명 |
|-----------------|-----------------------------------------------------|
| 회로차단기(circuit breaker) | 요청받은 서비스가 실패할 때 요청 중단 |
| 재시도(retry) | 서비스가 일시적으로 실패할 때 재시도 |
| 벌크헤드(bulkhead) | 과부하를 피하고자 동시 호출하는 서비스 요청 수 제한 |
| 속도 제한(rate limit) | 서비스가 한번에 수신하는 호출 수 제한 |
| 폴백(fallback) | 실패하는 요청에 대해 대체 경로 설정 |

##### 회복성 패턴의 순서
* Retry ( CircuitBreaker ( RateLimiter ( TimeLimiter ( Bulkhead ( Function ) ) ) ) )    
* bulkhead가 스레드 풀 생성 -> timelimiter가 스레드 시간 제한 -> ratelimiter가 해당 함수의 호출 횟수 제한 -> timelimiter & ratelimiter가 발생시킨 Exception을 circuitbreaker에 기록 및 회로 차단 -> 그 후 retry로 회복 시도

1. 서비스에 resilience4j 패키지 추가   
- `io.github.resilience4j` - `resilience4j-spring-boot2`    
- `io.github.resilience4j` - `resilience4j-circuitbreaker`    
- `io.github.resilience4j` - `resilience4j-timelimiter`     
2. AOP 관점을 실행하는 데 필요한 패키지 추가     
- `org.springframework.boot` - `spring-boot-starter-aop`      
3. Circuit breaker 를 적용하기 위한 `@circuitbreaker` 어노테이션 추가     
> com.spring.microservice.service.`LicenseService` 의 `getLicenseByOrganization` 함수 참조   
> circuit breaker를 테스트하기 위한 함수 추가(`randomlyRunLong`, `sleep` 함수 참조)    
4. 사용자 정의가 필요한 경우 `configserver`의 `license-service.properties` 의 `resilience4j` 키워드 참조     
*bulkhead 패턴의 thead pool 방식을 사용할 경우, CompletableFuture로 리턴 클래스(List<?>)를 감싸줘야함
```java
@Bulkhead(name = "bulkheadService", type = Type.THREADPOOL)
public CompletableFuture<List<model_class>> getData(){
  ...
}
```

---------------------------------------

# 스프링 클라우드 게이트웨이 (gatewayserver)   
1. `applicaion.properties` 설정 및 `configserver`의 `gateway-server.properties` 참조  
2. `http://localhost:8072/licensing-service/v1/organization/<organizationID>/license/<licenseID>`로 호출 가능
3. `http://localhost:8072/organization-service/v1/organization/<organizationID>` 또한 호출 가능
4. 사전 필터, 사후 필터 구현   
`microservice_licensing`의 `com.spring.microservice.utils` 패키지 및 `gatewayserver`의 `com.spring.gatewayserver.filter` 패키지 참조  
###### 필터의 순서    
`UserContextFilter` -> 유입되는 HTTP 요청을 가로채고, UserContext 클래스로 정보를 매핑하는 HTTP 서블렛 필터    
`UserContext` -> HTTP 헤더 값을 클래스 내부에 저장하기 위한 클래스   
`UserContextHolder` -> `ThreadLocal`에 UserContext 저장 (Thread는 스레드로 작업할 때 특정 객체의 모든 스레드는 변수를 공유한다. 이를 방지하기 위해 동기화를 사용하거나 ThreadLocal을 통해 동일한 스레드 에서만 읽고 쓸 수 있다.)   
`UserContextInterceptor` -> RestTemplate에서 실행되는 모든 서비스 발신에 정보를 주입.   

`ResponseFilter` -> HTTP 응답에 정보 추가 (게이트웨이 서비스는 클라이언트의 응답을 다시 검사하여 정보를 추가하거나, 마지막 로깅을 추가할 수 있다.)









