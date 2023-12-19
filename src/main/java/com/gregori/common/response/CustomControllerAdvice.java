package com.gregori.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gregori.common.exception.BaseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class CustomControllerAdvice {

	@ResponseBody
	@ExceptionHandler({ Exception.class, BaseException.class, MethodArgumentNotValidException.class })
	public <T> CustomResponse<T> onException(Exception e) {

		if (e instanceof BaseException) {

			HttpStatus httpStatus = ((BaseException)e).getErrorMessage().getHttpStatus();
			log.error("[Excetion] http status: {}, message: {} ", httpStatus, e.getMessage());

			return CustomResponse.failure(((BaseException)e).getErrorMessage());
		} else if (e instanceof MethodArgumentNotValidException) {

			HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
			log.error("[Excetion] http status: {}, message: {} ", httpStatus, e.getMessage());

			return CustomResponse.failure(httpStatus, e);
		}

		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		log.error("[Excetion] http status: {}, message: {} ", httpStatus, e.getMessage());

		return CustomResponse.failure(httpStatus, e);
	}
}
