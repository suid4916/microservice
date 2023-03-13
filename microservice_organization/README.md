# 마이크로 서비스 토커 통합


#### target 폴더에서 JAR을 찾는데 파일이 없다면 다음 명령어를 실행

	> mvn clean package

또는

	> mvnw clean package


#### 도커 이미지 빌드 명령어

	> mvn package dockerfile:build

또는

	> mvnw package dockerfile:build
	

#### 도커 빌드 후 이미지 확인 ~ 실행, 중단
* 이미지 확인

```
	> docker images
```
	
* 도커 실행

```
	> docker run ${REPOSITORY_name}:${TAG}
```
* 도커 백그라운드 실행

```
	> docker run -d ${REPOSITORY_name}:${TAG}
```
	
*도커 실행 및 네트워크 연결

```
	> docker run -itd -p8080:8080 ${REPOSITORY_name}:${TAG}
```

#### 도커 컨테이너 확인, 중단, 재실행
* 도커 컨테이너 확인(-a는 종료한 컨테이너까지 전부 확인)

```
	> docker ps -a
```
* 컨테이너 중단
	
```
	> docker stop ${CONTAINER_ID}
```
* 컨테이너 재실행

```
	> docker start -d ${CONTAINER_ID}
```