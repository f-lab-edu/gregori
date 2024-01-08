package com.gregori.member.domain;

import com.gregori.auth.domain.Authority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionMember {

	private Long id;
	private String email;
	private Authority authority;

	public SessionMember toEntity(Member member) {
		return SessionMember.builder()
			.id(member.getId())
			.email(member.getEmail())
			.authority(member.getAuthority())
			.build();
	}
}
