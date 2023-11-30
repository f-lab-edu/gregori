package com.gregori.refresh_token.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.refresh_token.domain.RefreshToken;

@Mapper
public interface RefreshTokenMapper {
	Long insert(RefreshToken refreshToken);
	Long update(RefreshToken refreshToken);
	Long delete(Long tokenId);
	Optional<RefreshToken> findByRtKey(String rtKey);
	Optional<RefreshToken> findById(Long tokenId);
}
