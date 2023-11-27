package com.gregori.common.response;

import com.gregori.common.exception.AppErrorMessage;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
public class CustomResponse<T> {
	private Result result;
	private T data;
	private String errorType;
	private String description;

	@Getter
	@RequiredArgsConstructor
	public enum Result {
		SUCCESS("성공"), FAILURE("실패");
		private final String description;
	}

	public static <T> CustomResponse<Object> success(T data, String description) {
		return CustomResponse.builder()
			.result(Result.SUCCESS)
			.data(data)
			.description(description)
			.build();
	}

	public static <T> CustomResponse<Object> success(T data) {
		return success(data, null);
	}

	public static CustomResponse<Object> failure(String errorType, String description) {
		return CustomResponse.builder()
			.result(Result.FAILURE)
			.errorType(errorType)
			.description(description)
			.build();
	}

	public static CustomResponse<Object> failure(AppErrorMessage appErrorMessage) {
		return CustomResponse.builder()
			.result(Result.FAILURE)
			.errorType(appErrorMessage.name())
			.description(appErrorMessage.getDescription())
			.build();
	}
}
