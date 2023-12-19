package com.gregori.common;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class AbstractEntity {

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;
}
