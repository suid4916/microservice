package com.spring.microservice.config;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class JWTConverterResourceAccess implements Converter<Jwt, AbstractAuthenticationToken> {
	
	@Override
	public AbstractAuthenticationToken convert(Jwt source) {
		Collection<GrantedAuthority> authorities = extractAuthorities(source);
		return new JwtAuthenticationToken(source, authorities);
	}

	@SuppressWarnings("unchecked")
	private Collection<GrantedAuthority> extractAuthorities(Jwt source) {
		if (source.getClaim("resource_access") != null) {
			Map<String, Object> resourceAccess = source.getClaim("resource_access");
			List<GrantedAuthority> authorities = new ArrayList<>();
			resourceAccess.keySet().forEach(id ->{
				if(resourceAccess.containsKey(id)) {
					Map<String, List<String>> resources = (Map<String, List<String>>) resourceAccess.get(id);
					resources.get("roles").forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_"+role)));
				}
			});
			return authorities;
		}
		return new ArrayList<>();
	}

}
