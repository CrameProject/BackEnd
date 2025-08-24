package com.backend.crame.domain.token.entity;

public interface OAuth2UserInfo {
	String getProvider();
	String getProviderId();
	String getEmail();
	String getName();
}
