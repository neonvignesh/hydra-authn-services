package com.enrich.hydra.authn.service;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.enrich.hydra.authn.constant.Constants;
import com.enrich.hydra.authn.pojo.StandardResponseMessage;
import com.enrich.hydra.authn.pojo.UserInfoPojo;
import com.enrich.hydra.authn.pojo.kambala.AuthenticateKambalaResponse;
import com.enrich.hydra.authn.pojo.kambala.GenerateOtpKambalaResponse;
import com.enrich.hydra.authn.pojo.kambala.LogoutKambalaResponse;
import com.enrich.hydra.authn.pojo.user.AuthenticateUserResponse;
import com.enrich.hydra.authn.pojo.user.GenerateOtpUserResponse;
import com.enrich.hydra.authn.pojo.user.LogoutUserResponse;
import com.enrich.hydra.authn.util.StandardResponseUtil;
import com.enrich.hydra.authn.util.Validator;
import com.enrich.hydra.authn.util.external.KambalaUtil;
import com.enrich.hydra.authn.util.external.ResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthenticateService {

	@Autowired
	private KambalaUtil kambalaUtil;
	
	public StandardResponseMessage authenticateService(UserInfoPojo client,HttpServletResponse response) {
		try {
			boolean validationResponse = validateParameterForAuthenticate(client);
			if (!validationResponse)
				return StandardResponseUtil.prepareBadRequestResponse(Constants.LOGIN_EERROR);
			else {
				ResponseEntity<String> loginOutput = kambalaUtil.authenticate(client);
				return processLoginResponse(loginOutput,response);
			}
		} catch (Exception e) {
			log.error("Error Occurred in  authenticateService : " + e.getMessage());
			 return StandardResponseUtil.prepareInternalServerErrorResponse(Constants.LOGIN_CUSTOMER_ERRR,response);
		}
	}

	private StandardResponseMessage processLoginResponse(ResponseEntity<String> loginOutput,HttpServletResponse response ) throws Exception {
		StandardResponseMessage standardResponse = new StandardResponseMessage();
		if (Validator.hasData(loginOutput)) {
			String loginResponse = loginOutput.getBody();
			ObjectMapper objectMapper = new ObjectMapper();
			AuthenticateKambalaResponse loginInfo = objectMapper.readValue(loginResponse,AuthenticateKambalaResponse.class);
			AuthenticateUserResponse userResponse = ResponseUtil.standardizeAuthenticateResponse(loginInfo);
			return handleLoginResponse(loginInfo.getStat(), loginInfo.getEmsg(), userResponse, standardResponse);
		} else
			  return StandardResponseUtil.prepareInternalServerErrorResponse(Constants.LOGIN_CUSTOMER_ERRR, response);
	}

	private StandardResponseMessage handleLoginResponse(String status, String message,
			AuthenticateUserResponse userResponse, StandardResponseMessage standardResponse) {
		if (Constants.NOT_OK.equals(status)) {
			if (Constants.INVALID_USER.equals(message) || Constants.WRONG_PASSWORD.equals(message)) {
				standardResponse = StandardResponseUtil.prepareUnAuthorizedResponse(userResponse);
			} else if (Constants.USER_BLOCKED.equals(message)) {
				standardResponse = StandardResponseUtil.prepareForbiddenResponse(userResponse);
			} else
				standardResponse = StandardResponseUtil.prepareBadRequestResponse(userResponse);
		} else if (Constants.OK.equals(status))
			standardResponse = StandardResponseUtil.prepareSuccessResponse(userResponse);
		else
			standardResponse = StandardResponseUtil.prepareInternalServerErrorResponse();
		return standardResponse;
	}

	private boolean validateParameterForAuthenticate(UserInfoPojo client) {
		if (!Validator.hasData(client.getUser_id()) || !Validator.hasData(client.getPassword()))
			return false;
		else
			return true;
	}

	public StandardResponseMessage logout(UserInfoPojo client, HttpServletRequest request) {
		try {
			String jKey = request.getHeader(Constants.X_TOKEN);
			if (!Validator.hasData(jKey))
				return StandardResponseUtil.prepareBadRequestResponse(Constants.X_TOKEN_ERROR);
			Boolean validationResponse = validateParameterForLogout(client);
			if (!validationResponse)
				return StandardResponseUtil.prepareBadRequestResponse(Constants.USER_ERROR);
			else {
				ResponseEntity<String> logoutOutput = kambalaUtil.getLogoutResponse(client, jKey);
				StandardResponseMessage logoutResponse = getLogoutResponse(logoutOutput);
				return logoutResponse;
			}
		} catch (Exception e) {
			log.error("Error Occured on logout service :", e.getMessage());
			return StandardResponseUtil.prepareInternalServerErrorResponse();
		}
	}

	private StandardResponseMessage getLogoutResponse(ResponseEntity<String> logoutOutput)
			throws JsonMappingException, JsonProcessingException, ParseException {
		StandardResponseMessage standardResponse = new StandardResponseMessage();
		if (logoutOutput.getStatusCode() == HttpStatus.UNAUTHORIZED)
			return StandardResponseUtil.prepareUnAuthorizedResponse();
		if (Validator.hasData(logoutOutput)) {
			String output = logoutOutput.getBody();
			ObjectMapper objectMapper = new ObjectMapper();
			LogoutKambalaResponse responseInfo = objectMapper.readValue(output, LogoutKambalaResponse.class);
			LogoutUserResponse userResponse = ResponseUtil.createLogoutUserResponse(responseInfo);
			standardResponse = prepareResponseBasedOnStatus(responseInfo.getStat(), responseInfo.getEmsg(),userResponse, standardResponse);
		} else
			return StandardResponseUtil.prepareInternalServerErrorResponse();
		return standardResponse;
	}

	private StandardResponseMessage prepareResponseBasedOnStatus(String status, String message,
			LogoutUserResponse userResponse, StandardResponseMessage standardResponse) {
		if (Constants.NOT_OK.equals(status)) {
			if (Constants.SESSION_EXPIRED.equals(message))
				standardResponse = StandardResponseUtil.prepareUnAuthorizedResponse(userResponse);
			else
				standardResponse = StandardResponseUtil.prepareBadRequestResponse(userResponse);
		} else if (Constants.OK.equals(status))
			standardResponse = StandardResponseUtil.prepareSuccessResponse(userResponse);
		return standardResponse;
	}

	private Boolean validateParameterForLogout(UserInfoPojo client) {
		if (!Validator.hasData(client.getUser_id()))
			return false;
		else
			return true;
	}
	
}
