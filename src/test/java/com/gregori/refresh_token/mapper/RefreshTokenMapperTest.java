package com.gregori.refresh_token.mapper;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gregori.common.CustomMybatisTest;
import com.gregori.refresh_token.domain.RefreshToken;

import static org.assertj.core.api.Assertions.assertThat;

@CustomMybatisTest
class RefreshTokenMapperTest {

	@Autowired
	private RefreshTokenMapper refreshTokenMapper;

	List<Long> tokenIds = new CopyOnWriteArrayList<>();

	@AfterEach
	void afterEach() {
		if (!tokenIds.isEmpty()) {
			tokenIds.forEach(tokenId -> refreshTokenMapper.deleteById(tokenId));
			tokenIds.clear();
		}
	}

	@Test
	@DisplayName("새로운 리프레시 토큰을 추가한다.")
	void should_insert() {

		// given
		RefreshToken refreshToken = new RefreshToken("key", "value");

		// when
		refreshTokenMapper.insert(refreshToken);
		tokenIds.add(refreshToken.getId());

		// then
		assertThat(refreshToken.getId()).isNotNull();
	}

	@Test
	@DisplayName("리프레시 토큰을 갱신한다.")
	void should_update() {

		// given
		RefreshToken refreshToken = new RefreshToken("key", "value");
		refreshTokenMapper.insert(refreshToken);
		tokenIds.add(refreshToken.getId());
		refreshToken.updateRefreshTokenValue("new value");

		// when
		refreshTokenMapper.update(refreshToken);

		// then
		Optional<RefreshToken> result = refreshTokenMapper.findById(refreshToken.getId());
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getRefreshTokenValue()).isEqualTo("new value");
	}

	@Test
	@DisplayName("id로 리프레시 토큰을 삭제한다.")
	void should_deleteById() {

		// given
		RefreshToken refreshToken = new RefreshToken("key", "value");

		// when
		refreshTokenMapper.deleteById(refreshToken.getId());

		// then
		Optional<RefreshToken> result = refreshTokenMapper.findById(refreshToken.getId());
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("id로 리프레시 토큰을 조회한다.")
	void should_findById() {

		// given
		RefreshToken refreshToken = new RefreshToken("key", "value");
		refreshTokenMapper.insert(refreshToken);
		tokenIds.add(refreshToken.getId());

		// when
		Optional<RefreshToken> result = refreshTokenMapper.findByRefreshTokenKey(refreshToken.getRefreshTokenKey());

		// then
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getRefreshTokenKey()).isEqualTo(refreshToken.getRefreshTokenKey());
	}

	@Test
	@DisplayName("refreshTokenKey로 조회한다.")
	void should_findByRefreshTokenKey() {

		// given
		RefreshToken refreshToken = new RefreshToken("key", "value");
		refreshTokenMapper.insert(refreshToken);
		tokenIds.add(refreshToken.getId());

		// when
		Optional<RefreshToken> result = refreshTokenMapper.findById(refreshToken.getId());

		// then
		assertThat(result.isPresent()).isTrue();
		assertThat(result.get().getId()).isEqualTo(refreshToken.getId());
	}
}