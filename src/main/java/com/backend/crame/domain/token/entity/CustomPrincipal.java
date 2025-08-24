package com.backend.crame.domain.token.entity;


import java.util.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomPrincipal implements UserDetails, OAuth2User {

	// 공통
	private final String uuid;
	private final String role;
	private final Collection<? extends GrantedAuthority> authorities;

	// 로컬 전용
	private final String username;
	private final String password;
	private final boolean accountNonExpired;
	private final boolean accountNonLocked;
	private final boolean credentialsNonExpired;
	private final boolean enabled;

	// 소셜 전용
	private final String provider;
	private final Map<String, Object> attributes;

	private CustomPrincipal(
		String uuid,
		String role,
		Collection<? extends GrantedAuthority> authorities,
		String username,
		String password,
		boolean accountNonExpired,
		boolean accountNonLocked,
		boolean credentialsNonExpired,
		boolean enabled,
		String provider,
		Map<String, Object> attributes
	) {
		this.uuid = uuid;
		this.role = role;
		this.authorities = authorities != null ? authorities : List.of();
		this.username = username;
		this.password = password; // 로컬일 때만 세팅(해시)
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
		this.provider = provider != null ? provider : "LOCAL";
		this.attributes = attributes != null ? attributes : Map.of();
	}

	/* ========= 정적 팩토리 ========= */

	/** 로컬 로그인용 생성기 */
	public static CustomPrincipal fromLocal(String uuid,
		String username,
		String passwordHash,
		String role) {
		return new CustomPrincipal(
			uuid,
			role,
			List.of(new SimpleGrantedAuthority(role)),
			username,
			passwordHash, // 반드시 해시
			true, true, true, true,
			"LOCAL",
			Map.of()
		);
	}

	/** 소셜 로그인용 생성기 */
	public static CustomPrincipal fromOAuth(String uuid,
		String role,
		String provider,
		Map<String, Object> attributes,
		Collection<? extends GrantedAuthority> authorities) {
		return new CustomPrincipal(
			uuid,
			role,
			authorities != null ? authorities : List.of(new SimpleGrantedAuthority(role)),
			null,              // username 없음
			null,              // password 없음
			true, true, true, true,
			provider != null ? provider.toUpperCase() : "SOCIAL",
			attributes
		);
	}

	/* ========= 공통/도메인 ========= */

	public String getUserId() {
		return uuid;
	}

	public String getProvider() {
		return provider;
	}

	public String getRole() {
		return role;
	}

	/* ========= OAuth2User ========= */

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/** Security에서의 사용자 표시용 이름(안정적 식별자 권장) */
	@Override
	public String getName() {
		return uuid;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	/* ========= UserDetails(로컬) ========= */

	@Override
	public String getPassword() {
		return password; // 로컬일 때만 값 존재
	}

	@Override
	public String getUsername() {
		return username; // 로컬일 때만 값 존재
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
