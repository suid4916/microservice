#스프링 클라우드 컨피그 서버

### 스프링 클라우드 컨피그 서버 옵션


- 파일(resources/config) -> (native)application.properties
- git -> (git_and_native)application.properties
- Vault -> (vault)application.properties

### Vault 서버 적용


* 1. 도커를 이용하여 Vault 컨테이너 생성

	docker run -d -p 8200:8200 --name vaule -e VAULT_DEV_ROOT_TOKEN_ID=myroot -e VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200 vault

	**-e 변수에 ' 를 쓸경우 잘못 인식됨.** (~~-e 'VAULT_DEV_ROOT_TOKEN_ID=myroot'~~)

* 2. http://localhost:8200/ui/vault/auth로 로그인

* 3. KV 엔진으로 생성, path는 licensing-service, version은 2

* 4. create secret 에 Path for this secret은 licensing-service, secret data는 각각 license.vault.property, Welcome to Valut로 생성

### 대칭 암호화 설정

* bootstrap.properties 파일 생성 후 파일 안에 encrypt.key 설정
	
