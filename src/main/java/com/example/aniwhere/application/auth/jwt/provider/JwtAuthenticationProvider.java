package com.example.aniwhere.application.auth.jwt.provider;

import com.example.aniwhere.application.auth.jwt.dto.Claims;
import com.example.aniwhere.application.auth.jwt.dto.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider {

	private final TokenProvider tokenProvider;

	public Authentication authentication(final String accessToken) {
		Claims claims = tokenProvider.validateToken(accessToken);
		JwtAuthentication jwtAuthentication = new JwtAuthentication(claims.userId(), accessToken);
		List<GrantedAuthority> authorities = getAuthorities(claims.authorities());
		return new UsernamePasswordAuthenticationToken(jwtAuthentication, accessToken, authorities);
	}

	private List<GrantedAuthority> getAuthorities(final List<String> authorities) {
		return authorities.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}
}
