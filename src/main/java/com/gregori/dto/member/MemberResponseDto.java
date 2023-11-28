package com.gregori.dto.member;

import com.gregori.domain.member.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
	private Long id;
	private String email;
	private String name;
	private String status;

	public MemberResponseDto toEntity(Member member) {
		return MemberResponseDto.builder()
			.id(member.getId())
			.name(member.getName())
			.email(member.getEmail())
			.status(member.getStatus().toString())
			.build();
	}
}
