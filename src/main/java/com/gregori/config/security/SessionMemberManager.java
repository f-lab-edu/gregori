package com.gregori.config.security;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.gregori.common.exception.NotFoundException;
import com.gregori.common.exception.ValidationException;
import com.gregori.member.domain.Member;
import com.gregori.member.domain.SessionMember;
import com.gregori.member.mapper.MemberMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SessionMemberManager {

	private final MemberMapper memberMapper;

	public SessionMember getSessionMember(HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		if (session == null) {
			throw new NotFoundException("세션을 찾을 수 없습니다.");
		}

		return (SessionMember)session.getAttribute("member");
	}

	public void sessionMemberValidation(SessionMember sessionMember) {

		Member member = memberMapper.findById(sessionMember.getId()).orElseThrow(NotFoundException::new);
		if (!Objects.equals(sessionMember.getId(), member.getId()) ||
			!StringUtils.equals(sessionMember.getEmail(), member.getEmail()) ||
			!Objects.equals(sessionMember.getAuthority(), member.getAuthority())) {
			throw new ValidationException("세션 정보가 일치하지 않습니다.");
		}
	}
}
