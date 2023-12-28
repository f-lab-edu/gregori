package com.gregori.config.security;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.gregori.auth.domain.Authority;
import com.gregori.auth.domain.Login;
import com.gregori.member.domain.SessionMember;
import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.UnauthorizedException;
import com.gregori.common.exception.ValidationException;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

	private final MemberMapper memberMapper;

	public AuthArgumentResolver(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {

		boolean assignableFrom = parameter.getParameterType().isAssignableFrom(SessionMember.class);

		return parameter.hasParameterAnnotation(Login.class) && assignableFrom;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		@NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		SessionMember sessionMember = getSessionMember(webRequest);
		checkValidation(sessionMember);

		Authority[] authorities = Objects
			.requireNonNull(parameter.getParameterAnnotation(Login.class))
			.authority();
		if (!List.of(authorities).contains(sessionMember.getAuthority())) {
			throw new UnauthorizedException("접근 권한이 없습니다.");
		}

		return sessionMember;
	}

	private SessionMember getSessionMember(final NativeWebRequest webRequest) {

		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		HttpSession session = request.getSession(false);
		if (session == null) {
			throw new NotFoundException("세션을 찾을 수 없습니다.");
		}

		return (SessionMember)session.getAttribute("member");
	}

	private void checkValidation(SessionMember sessionMember) {

		Member member = memberMapper.findById(sessionMember.getId()).orElseThrow(NotFoundException::new);
		if (!Objects.equals(sessionMember.getId(), member.getId()) ||
			!StringUtils.equals(sessionMember.getEmail(), member.getEmail()) ||
			!Objects.equals(sessionMember.getAuthority(), member.getAuthority())) {
			throw new ValidationException("세션 정보가 일치하지 않습니다.");
		}
	}
}
