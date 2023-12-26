package com.gregori.config.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.UnauthorizedException;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;
import com.gregori.auth.mapper.RefreshTokenMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.gregori.member.domain.Member.Status.DEACTIVATE;
import static java.lang.Long.parseLong;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String BEARER_PREFIX = "Bearer";
	private final TokenProvider tokenProvider;
	private final RefreshTokenMapper refreshTokenMapper;
	private final MemberMapper memberMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String requestURI = request.getRequestURI();
		String jwt = resolveToken(request);

		if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
			Authentication authentication = tokenProvider.getAuthentication(jwt);
			String username = authentication.getName();
			Member member = memberMapper.findById(parseLong(username)).orElseThrow(NotFoundException::new);

			if (member.getStatus() == DEACTIVATE) {
				throw new UnauthorizedException("탈퇴한 사용자입니다.");
			}

			refreshTokenMapper.findByRefreshTokenKey(username)
				.orElseThrow(() -> new UnauthorizedException("로그아웃한 사용자입니다."));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
		} else {
			log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
		}
		filterChain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {

		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(7);
		}

		return null;
	}
}
