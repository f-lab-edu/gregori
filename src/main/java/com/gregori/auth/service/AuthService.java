package com.gregori.auth.service;

import com.gregori.auth.dto.AuthSignInDto;
import com.gregori.auth.dto.TokenDto;
import com.gregori.auth.dto.TokenRequestDto;

public interface AuthService {
	TokenDto signIn(AuthSignInDto memberSignInDto);
	Long signOut(TokenRequestDto tokenRequestDto);
	TokenDto refresh(TokenRequestDto tokenRequestDto);
}
