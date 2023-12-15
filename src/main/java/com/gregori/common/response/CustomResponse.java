package com.gregori.common.response;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
public class CustomResponse<T> {
	private Result result;
	private T data;
	private HttpStatus httpStatus;
	private String errorType;
	private String description;

	@Getter
	@RequiredArgsConstructor
	public enum Result {
		SUCCESS("성공"), FAILURE("실패");
		private final String description;
	}

	public static <T> CustomResponse<T> success(T data, SuccessMessage successMessage) {
		return CustomResponse.<T>builder()
			.result(Result.SUCCESS)
			.httpStatus(successMessage.getHttpStatus())
			.data(data)
			.description(successMessage.getDescription())
			.build();
	}

	public static <T> CustomResponse<T> failure(ErrorMessage errorMessage) {
		return CustomResponse.<T>builder()
			.result(Result.FAILURE)
			.httpStatus(errorMessage.getHttpStatus())
			.errorType(errorMessage.name())
			.description(errorMessage.getDescription())
			.build();
	}

	public static <T> CustomResponse<T> failure(HttpStatus httpStatus, Exception e) {
		return CustomResponse.<T>builder()
			.result(Result.FAILURE)
			.httpStatus(httpStatus)
			.errorType(null)
			.description(e.getMessage())
			.build();
	}
}
