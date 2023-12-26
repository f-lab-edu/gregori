package com.gregori.member.dto;

import com.gregori.common.domain.IsDeleted;
import com.gregori.member.domain.Member;

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
	private IsDeleted isDeleted;

	public MemberResponseDto toEntity(Member member) {

		return MemberResponseDto.builder()
			.id(member.getId())
			.name(member.getName())
			.email(member.getEmail())
			.isDeleted(member.getIsDeleted())
			.build();
	}
}
