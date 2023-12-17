package com.gregori.refresh_token.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gregori.common.CustomMybatisTest;
import com.gregori.refresh_token.domain.RefreshToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@CustomMybatisTest
class RefreshTokenMapperTest {

	@Autowired
	private RefreshTokenMapper refreshTokenMapper;

	List<Long> tokenIds = new ArrayList<>();

	@AfterEach
	void afterEach() {
		if (!tokenIds.isEmpty()) {
			tokenIds.stream().map(tokenId -> refreshTokenMapper.deleteById(tokenId));
			tokenIds.clear();
		}
	}

	@Test
	@DisplayName("refreshToken을 입력 받으면 새로운 리프레시 토큰을 추가한다.")
	void should_create_when_refreshTokenInput() {

		// given
		RefreshToken refreshToken = new RefreshToken("key", "value");

		// when
		refreshTokenMapper.insert(refreshToken);
		tokenIds.add(refreshToken.getId());

		// then
		assertNotNull(refreshToken.getId());
	}

	@Test
	@DisplayName("refreshToken을 입력 받으면 리프레시 토큰을 갱신한다.")
	void should_updateRefreshTokeValue_when_refreshTokenInput() {

		// given
		RefreshToken refreshToken = new RefreshToken("key", "value");
		refreshTokenMapper.insert(refreshToken);
		tokenIds.add(refreshToken.getId());
		refreshToken.updateRefreshTokenValue("new value");

		// when
		refreshTokenMapper.update(refreshToken);

		// then
		Optional<RefreshToken> result = refreshTokenMapper.findById(refreshToken.getId());
		assertTrue(result.isPresent());
		assertEquals(result.get().getRefreshTokenValue(), "new value");
	}

	@Test
	@DisplayName("id를 입력 받으면 id와 일치하는 리프레시 토큰을 삭제한다.")
	void should_delete_when_idInput() {

		// given
		RefreshToken refreshToken = new RefreshToken("key", "value");

		// when
		refreshTokenMapper.deleteById(refreshToken.getId());

		// then
		Optional<RefreshToken> result = refreshTokenMapper.findById(refreshToken.getId());
		assertTrue(result.isEmpty());
	}

	@Test
	@DisplayName("refreshTokenKey를 입력 받으면 refreshTokenKey와 일치하는 리프레시 토큰을 조회한다.")
	void should_find_when_refreshTokenKeyInput() {

		// given
		RefreshToken refreshToken = new RefreshToken("key", "value");
		refreshTokenMapper.insert(refreshToken);
		tokenIds.add(refreshToken.getId());

		// when
		Optional<RefreshToken> result = refreshTokenMapper.findByRefreshTokenKey(refreshToken.getRefreshTokenKey());

		// then
		assertTrue(result.isPresent());
		assertEquals(result.get().getRefreshTokenKey(), refreshToken.getRefreshTokenKey());
	}

	@Test
	@DisplayName("id를 입력 받으면 id와 일치하는 리프레시 토큰을 조회한다.")
	void should_find_when_idInput() {

		// given
		RefreshToken refreshToken = new RefreshToken("key", "value");
		refreshTokenMapper.insert(refreshToken);
		tokenIds.add(refreshToken.getId());

		// when
		Optional<RefreshToken> result = refreshTokenMapper.findById(refreshToken.getId());

		// then
		assertTrue(result.isPresent());
		assertEquals(result.get().getId(), refreshToken.getId());
	}
}