package com.gregori.config.security;

import java.util.List;
import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.gregori.auth.domain.Authority;
import com.gregori.auth.domain.LoginCheck;
import com.gregori.common.exception.UnauthorizedException;
import com.gregori.member.domain.SessionMember;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthIntercepter implements HandlerInterceptor {

	private final SessionMemberManager manager;

	@Override
	public boolean preHandle(@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		LoginCheck loginCheck = handlerMethod.getMethodAnnotation(LoginCheck.class);
		if (loginCheck == null) {
			return true;
		}

		Authority[] authorities = Objects.requireNonNull(loginCheck).authority();
		SessionMember sessionMember = manager.getSessionMember(request);
		if (!List.of(authorities).contains(sessionMember.getAuthority())) {
			throw new UnauthorizedException("접근 권한이 없습니다.");
		}

		return true;
	}
}
