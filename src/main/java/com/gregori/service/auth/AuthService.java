package com.gregori.service.auth;

import com.gregori.dto.auth.AuthSignInDto;
import com.gregori.dto.auth.TokenDto;
import com.gregori.dto.auth.TokenRequestDto;

public interface AuthService {
	TokenDto signIn(AuthSignInDto memberSignInDto);
	Long signOut(TokenRequestDto tokenRequestDto);
	TokenDto refresh(TokenRequestDto tokenRequestDto);
}
