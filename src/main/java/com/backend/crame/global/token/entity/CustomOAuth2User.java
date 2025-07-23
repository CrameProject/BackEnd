package com.backend.crame.global.token.entity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

	private final Map<String, Object> attributes; //여기에 인증서버로 부터 받은 사용자 정보가 담김

	private final String uuid;

	private final String role;

	public CustomOAuth2User(Map<String, Object> attributes, String uuid, String role) {
		this.attributes = attributes;
		this.uuid = uuid;
		this.role = role;
	}

	public String getUserId() {
		return uuid;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return uuid;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(() -> role);
	}


}
