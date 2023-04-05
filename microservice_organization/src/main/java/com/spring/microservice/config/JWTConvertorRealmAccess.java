package com.spring.microservice.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class JWTConvertorRealmAccess implements Converter<Jwt, Collection<GrantedAuthority>> {
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<GrantedAuthority> convert(Jwt source) {
		// TODO Auto-generated method stub
		final Map<String, Object> realmAccess = (Map<String, Object>) source.getClaims().get("realm_access");
		return ((List<String>) realmAccess.get("roles")).stream().map(roleName -> "ROLE_" + roleName)
				.map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

}
