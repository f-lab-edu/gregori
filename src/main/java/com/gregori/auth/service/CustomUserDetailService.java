package com.gregori.auth.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gregori.common.exception.NotFoundException;
import com.gregori.member.domain.Member;
import com.gregori.member.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
	private final MemberMapper memberMapper;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return memberMapper.findByEmail(username)
			.map(this::createUserDetails)
			.orElseThrow(NotFoundException::new);
	}

	private UserDetails createUserDetails(Member member) {
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(grantedAuthority);

		return new User(String.valueOf(member.getId()),
			member.getPassword(), authorities);
	}
}
