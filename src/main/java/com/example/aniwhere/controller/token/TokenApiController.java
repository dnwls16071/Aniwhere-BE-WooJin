package com.example.aniwhere.controller.token;

//import com.example.aniwhere.service.user.KakaoService;
import com.example.aniwhere.service.token.TokenService;
import com.example.aniwhere.domain.token.dto.OAuthToken;
import com.example.aniwhere.application.config.CookieConfig;
import com.example.aniwhere.service.user.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "Token", description = "토큰 관련 API")
@RequiredArgsConstructor
public class TokenApiController {

	private final TokenService tokenService;
	private final CookieConfig cookieConfig;
	private final KakaoService kakaoService;

	@Operation(
			summary = "액세스 토큰 재발급 (스프링 시큐리티)",
			description = "만료된 액세스 토큰을 재발급받기 위한 API - 스프링 시큐리티 자체 서비스"
	)
	@PostMapping("/reissue")
	public ResponseEntity<ResponseCookie> createNewAccessToken(HttpServletRequest request) {

		String refreshToken = cookieConfig.extractRefreshToken(request);
		ResponseCookie newAccessToken = tokenService.createNewAccessToken(refreshToken);

		return ResponseEntity.status(HttpStatus.CREATED)
				.header(HttpHeaders.SET_COOKIE, newAccessToken.toString())
				.build();
	}

	@Operation(
			summary = "액세스 토큰 / 리프레시 토큰 재발급 (카카오)",
			description = "액세스 토큰과 리프레시 토큰을 재발급받기 위한 API - 카카오 소셜 로그인 서비스"
	)
	@GetMapping("/kakaoreissue")
	public Mono<ResponseEntity<Void>> createNewAccessTokenByKakao(HttpServletRequest request, HttpServletResponse response) {

		String refreshToken = cookieConfig.extractRefreshToken(request);
		return kakaoService.kakaoReissue(refreshToken)
				.map(token -> ResponseEntity.status(HttpStatus.CREATED)
						.header(HttpHeaders.SET_COOKIE, token.accessToken())
						.header(HttpHeaders.SET_COOKIE, token.refreshToken())
						.build());
	}
}
