package com.gregori.dto.member;

import static com.gregori.common.RegexPatterns.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.gregori.domain.member.Member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegisterDto {
	@NotEmpty(message = "name은 필수값입니다.")
	@Pattern(regexp = NAME_REGEX, message = "name 형식이 일치해야 합니다.")
	private String name;

	@NotEmpty(message = "email은 필수값입니다.")
	@Email(message = "email 형식과 일치해야 합니다.")
	private String email;

	@NotEmpty(message = "password는 필수값입니다.")
	@Pattern(regexp = PASSWORD_REGEX, message = "password 형식이 일치해야 합니다.")
	private String password;

	public Member toEntity(PasswordEncoder passwordEncoder) {
		return Member.builder()
			.name(name)
			.email(email)
			.password(passwordEncoder.encode(password))
			.build();
	}
}
