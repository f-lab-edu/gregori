package com.gregori.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.gregori.domain.auth.Session;

@Mapper
public interface SessionMapper {
	Long insert(Session session);
	Long delete(String sessionId);
	Optional<Session> findById(String sessionId);
}
