package com.gregori.domain;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class AbstractEntity {

    private final ZonedDateTime createdAt = ZonedDateTime.now();

    private final ZonedDateTime updatedAt = ZonedDateTime.now();
}
