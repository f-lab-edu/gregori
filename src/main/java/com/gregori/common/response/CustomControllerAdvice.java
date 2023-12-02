package com.gregori.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gregori.common.exception.BaseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class CustomControllerAdvice {
	@ResponseBody
	@ExceptionHandler(value = Exception.class)
	public <T> CustomResponse<T> onException(Exception e) {
		log.error("[Excetion] http status: {}, message: {} ",
			HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

		return CustomResponse.failure(ErrorMessage.SYSTEM_ERROR);
	}

	@ResponseBody
	@ExceptionHandler(value = BaseException.class)
	public <T> CustomResponse<T> onException(BaseException e) {
		log.error("[BaseException] http status: {}, custom status: {}, message: {} ",
			e.getErrorMessage().getHttpStatus(), e.getErrorMessage().name(), e.getErrorMessage().getDescription());

		return CustomResponse.failure(e.getErrorMessage());
	}
}
