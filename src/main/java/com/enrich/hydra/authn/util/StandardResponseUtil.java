package com.enrich.hydra.authn.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.enrich.hydra.authn.common.ApiResponse;
import com.enrich.hydra.authn.constant.Constants;
import com.enrich.hydra.authn.pojo.StandardResponseMessage;
import com.enrich.hydra.authn.pojo.user.LogoutUserResponse;

public class StandardResponseUtil {

	public static StandardResponseMessage prepareInternalServerErrorResponse(String data, HttpServletResponse response) {
	    StandardResponseMessage standardResponse = new StandardResponseMessage();
	    standardResponse.setSuccess(Constants.FALSE);
	    standardResponse.setSystemMessage(Constants.MESSAGE_TYPE_500_INTERNAL_SERVER_ERROR);
	    standardResponse.setSystemMessageType(Constants.MESSAGE_TYPE_FAILED);
	    standardResponse.setErrorMessage(data);
	    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); 
	    return standardResponse;
	}


	public static StandardResponseMessage prepareInternalServerErrorResponse() {
		StandardResponseMessage standardResponse = new StandardResponseMessage();
		standardResponse.setSuccess(Constants.FALSE);
		standardResponse.setSystemMessage(Constants.MESSAGE_TYPE_500_INTERNAL_SERVER_ERROR);
		standardResponse.setSystemMessageType(Constants.MESSAGE_TYPE_FAILED);
		return standardResponse;
	}
	
	public static StandardResponseMessage prepareBadRequestResponse(ApiResponse data) {
		StandardResponseMessage standardResponse = new StandardResponseMessage();
		standardResponse.setSuccess(Constants.FALSE);
		standardResponse.setSystemMessage(Constants.MESSAGE_TYPE_400_BAD_REQUEST_ERROR);
		standardResponse.setSystemMessageType(Constants.MESSAGE_TYPE_FAILED);
		standardResponse.setData(data);
		return standardResponse;
	}
	
	public static StandardResponseMessage prepareBadRequestResponse(String data) {
		StandardResponseMessage standardResponse = new StandardResponseMessage();
		
		LogoutUserResponse response = new LogoutUserResponse();
		response.setError_message(data);
		standardResponse.setSuccess(Constants.FALSE);
		standardResponse.setSystemMessage(Constants.MESSAGE_TYPE_400_BAD_REQUEST_ERROR);
		standardResponse.setSystemMessageType(Constants.MESSAGE_TYPE_FAILED);
		standardResponse.setData(response);
		return standardResponse;
	}
	
	

	public static StandardResponseMessage prepareUnAuthorizedResponse(ApiResponse data) {
		StandardResponseMessage standardResponse = new StandardResponseMessage();
		standardResponse.setSuccess(Constants.FALSE);
		standardResponse.setSystemMessage(Constants.MESSAGE_TYPE_401_UNAUTHORIZED_ERROR);
		standardResponse.setSystemMessageType(Constants.MESSAGE_TYPE_FAILED);
		standardResponse.setData(data);
		return standardResponse;
	}
	
	public static StandardResponseMessage prepareForbiddenResponse(ApiResponse data) {
	    StandardResponseMessage standardResponse = new StandardResponseMessage();
	    standardResponse.setSuccess(Constants.FALSE);
	    standardResponse.setSystemMessage(Constants.MESSAGE_TYPE_403_FORBIDDEN_ERROR);
	    standardResponse.setSystemMessageType(Constants.MESSAGE_TYPE_FAILED);
	    standardResponse.setData(data);
	    return standardResponse;
	}

	public static StandardResponseMessage prepareUnAuthorizedResponse() {
		StandardResponseMessage standardResponse = new StandardResponseMessage();
		standardResponse.setSuccess(Constants.FALSE);
		standardResponse.setSystemMessage(Constants.MESSAGE_TYPE_401_UNAUTHORIZED_ERROR);
		standardResponse.setSystemMessageType(Constants.MESSAGE_TYPE_FAILED);
		return standardResponse;
	}

	public static StandardResponseMessage prepareSuccessResponse(ApiResponse data) {
		StandardResponseMessage standardResponse = new StandardResponseMessage();
		standardResponse.setSuccess(Constants.TRUE);
		standardResponse.setSystemMessage(Constants.MESSAGE_TYPE_200);
		standardResponse.setSystemMessageType(Constants.MESSAGE_TYPE_SUCCESS);
		standardResponse.setData(data);
		return standardResponse;
	}

	public static ResponseEntity<StandardResponseMessage> generateResponseEntity(StandardResponseMessage result) {
	    HttpStatus status;
	    if (result.getSystemMessage().equals(Constants.MESSAGE_TYPE_403_FORBIDDEN_ERROR)) {
	        status = HttpStatus.FORBIDDEN;
	    } else if (result.getSystemMessage().equals(Constants.MESSAGE_TYPE_401_UNAUTHORIZED_ERROR)) {
	        status = HttpStatus.UNAUTHORIZED;
	    } else if (result.getSystemMessage().equals(Constants.MESSAGE_TYPE_400_BAD_REQUEST_ERROR)) {
	        status = HttpStatus.BAD_REQUEST;
	    } else {
	        status = HttpStatus.OK;
	    }    
	    return ResponseEntity.status(status).body(result);
	}

	public static StandardResponseMessage prepareInternalServerErrorResponse(String data) {
		StandardResponseMessage standardResponse = new StandardResponseMessage();
		standardResponse.setSuccess(Constants.FALSE);
		standardResponse.setSystemMessage(Constants.MESSAGE_TYPE_500_INTERNAL_SERVER_ERROR);
		standardResponse.setSystemMessageType(Constants.MESSAGE_TYPE_FAILED);
		standardResponse.setErrorMessage(data);
		return standardResponse;	
	}

	public static StandardResponseMessage prepareErrorResponse(String message) {
		StandardResponseMessage standardResponse = new StandardResponseMessage();
		standardResponse.setSuccess(Constants.FALSE);
		standardResponse.setSystemMessage(message);
		standardResponse.setSystemMessageType(Constants.MESSAGE_TYPE_FAILED);
		return standardResponse;
	}
}
