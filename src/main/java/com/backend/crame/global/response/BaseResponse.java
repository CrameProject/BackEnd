package com.backend.crame.global.response;

import org.springframework.http.ResponseEntity;

import com.backend.crame.global.response.dto.ResponseDto;
import com.backend.crame.global.exception.ErrorCode;
import com.backend.crame.global.response.enums.SuccessCode;

public class BaseResponse {

	public static <T> ResponseEntity<ResponseDto<T>> success(SuccessCode successCode,T data){
		return ResponseEntity.status(successCode.getCode())
			.body(new ResponseDto<>(successCode.getCode(),successCode.getMessage(),data));
	}

	public static ResponseEntity<ResponseDto<Void>> fail(ErrorCode errorCode){
		return ResponseEntity.status(errorCode.getCode())
			.body(new ResponseDto<>(errorCode.getCode(), errorCode.getMessage(),null));
	}

}
