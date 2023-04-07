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
spring.config.import=optional:configserver:      
spring.cloud.config.uri=http://localhost:8071      
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
`KV Engine`으로 새 엔진 생성, path는 `licensing-service`, version은 2

