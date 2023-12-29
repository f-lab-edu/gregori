package com.gregori.config.security;

import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.gregori.auth.domain.CurrentMember;
import com.gregori.auth.domain.LoginCheck;
import com.gregori.member.domain.SessionMember;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

	private final SessionMemberManager manager;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {

		boolean assignableFrom = parameter.getParameterType().isAssignableFrom(SessionMember.class);

		return parameter.hasParameterAnnotation(CurrentMember.class) && assignableFrom;
	}

	@Override
	public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
		@NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();

		SessionMember sessionMember = manager.getSessionMember(request);
		if (!parameter.hasMethodAnnotation(LoginCheck.class)) {
			manager.sessionMemberValidation(sessionMember);
		}

		return sessionMember;
	}
}
