package com.spring.microservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)

public class WebSecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests().anyRequest().authenticated();
//		http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
		http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(new JWTConverterResourceAccess());
		return http.build();
	}

//	private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
//		// TODO Auto-generated method stub
//		JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
//		jwtConverter.setJwtGrantedAuthoritiesConverter(new JWTConvertorRealmAccess());
//		return jwtConverter;
//	}
}
