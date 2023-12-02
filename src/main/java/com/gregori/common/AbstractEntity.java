package com.gregori.common;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class AbstractEntity {
    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;
}
