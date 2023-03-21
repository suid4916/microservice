#stage1
#start with a base image containing JAVA runtime
FROM openjdk:11-slim as build

#add maintainer info
LABEL maintainer="suid4916"

#the application's jar file
#dockerfile-maven-plugin에 설정된 JAR_FILE 변수를 정의
ARG JAR_FILE 

#add the application's jar to the container
#JAR 파일을 이미지의 파일 시스템에 app.jar로 복사
COPY ${JAR_FILE} app.jar 

#unpackage jar file
#빌드 이미지의 파일 시스템에 복사한 app.jar의 압축 풀기
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar) 

#stage2 신규 이미지는 스프링 부트 앱에 대한 통짜 JAR 파일 대신 여러 레이어로 구성
#same java runtime
FROM openjdk:11-slim

#add volume pointing to /tmp
VOLUME /tmp

#copy unpackaged application to new container - stage 1에서 build라고 명명된 첫 이미지에서 여러 레이어를 복사
ARG DEPENDENCY=/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

#execute the application
#컨테이너가 생성될 때 이미지의 라이선싱 서비스 애플리케이션을 실행 대상으로 지정s
ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.spring.microservice.OrganizationServiceApplication"]