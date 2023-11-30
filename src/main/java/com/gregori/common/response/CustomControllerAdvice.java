package com.gregori.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gregori.common.exception.ErrorMessage;
import com.gregori.common.exception.BaseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class CustomControllerAdvice {
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Exception.class)
	public CustomResponse onException(Exception e) {
		log.error("[Excetion] status = {Internal Server Error} message = {} ", e.getMessage());
		return CustomResponse.failure(ErrorMessage.SYSTEM_ERROR);
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(value = BaseException.class)
	public CustomResponse onBaseException(BaseException e) {
		log.error("[BaseException] errorType = {}, errorDescription = {} ", e.getErrorMessage().name(), e.getMessage());
		return CustomResponse.failure(e.getErrorMessage().name(), e.getMessage());
	}
}
